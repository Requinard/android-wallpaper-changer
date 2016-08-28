package com.terarion.wallpaper_changer.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.WallpaperChangerReceiver
import com.terarion.wallpaper_changer.model.DataHolder
import com.terarion.wallpaper_changer.ui.fragments.MasterViewPager
import com.terarion.wallpaper_changer.util.delegators.view
import java.io.File

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 100
    val FOLDER_NAME = "WallpaperChanger"
    val data = DataHolder()

    var directories = emptyList<String>()
    val filespinner by view(Spinner::class.java)
    val fab by view(FloatingActionButton::class.java)
    val fab2 by view(FloatingActionButton::class.java)

    val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    val listener = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            Log.d(tag, "Preference ${key} has changed")
            // If the enabled setting has been enabled, schedule a pape change
            if (preferences.getBoolean("enabled", false) == true) {
                WallpaperChangerReceiver().schedule(this@MainActivity)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE)

//            sendBroadcast(Intent(this, WallpaperChangerReceiver::class.java))
        }

        fab2.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                    .setView(editText)
                    .setPositiveButton("Create", { a, b ->
                        File(Environment.getExternalStorageDirectory(), "${FOLDER_NAME}/${editText.text.toString()}").mkdirs()
                        data.update()
                        addFolders()
                    })
                    .setNegativeButton("Cancel", { a, b -> a.cancel() })
                    .show()
            WallpaperChangerReceiver().schedule(this)
        }

        if (intent.hasExtra(Intent.EXTRA_STREAM)) {
            handleIncomingIntent(intent)
        }

        addFolders()

        addAlbums()

        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun handleIncomingIntent(intent: Intent) {
        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        Log.d(tag, "Received uri ${uri.toString()}")
        val spinner = Spinner(this)
        spinner.adapter = object : ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, directories) {}
        AlertDialog.Builder(this)
                .setView(spinner)
                .setPositiveButton("Add", { a, b -> Log.d(tag, "accepted") })
                .setNegativeButton("Cancel", { a, b -> a.cancel() })
                .show()
    }

    private fun addAlbums() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.recycler_holder, MasterViewPager())
                .commitAllowingStateLoss()
    }

    private val tag = "Main Activity"


    private fun addFolders() {
        directories = emptyList()

        data.albums.forEach { directories += it.name }
        Log.d(tag, "${directories}")

        filespinner.adapter = object : ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, directories) {}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.data != null) {
                val uri = data.data

                Log.d(tag, data.toString())

                moveToFolder(uri, directories[filespinner.selectedItemPosition])
            } else if (data.clipData != null) {
                val clipData = data.clipData

                var i = 0

                while (i != clipData.itemCount) {
                    moveToFolder(clipData.getItemAt(i).uri, directories[filespinner.selectedItemPosition])
                    i++
                }
            } else {
                Log.d("lol", "didn't find anything")
            }
        }
    }

    private fun moveToFolder(uri: Uri, directory: String) {
        val filePathColumn = arrayOf<String>(MediaStore.Images.Media.DATA)

        object : AsyncTask<Unit, File, File>() {
            override fun doInBackground(vararg params: Unit?): File {
                // Create the directory
                val directory = File(Environment.getExternalStorageDirectory(), "${FOLDER_NAME}/$directory")

                directory.mkdirs()

                val cursor = contentResolver.openInputStream(uri)

                val fileOld = File(uri.path)
                val fileNew = File(directory, "${fileOld.hashCode()}.png")

                cursor.copyTo(fileNew.outputStream())

                return fileNew
            }

            override fun onPostExecute(result: File?) {
                Snackbar.make(fab, "Saved image!", Snackbar.LENGTH_SHORT)
            }
        }.execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
