package com.awkitsune.notes

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.*

import android.os.Environment

import android.net.Uri

import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NoteAdapter
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var settings: SharedPreferences

    private lateinit var view: View
    private lateinit var dialog: Dialog

    @ExperimentalStdlibApi
    private val selectFileToImportResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = contentResolver.openInputStream(uri)

                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var stringBuilder = StringBuilder()
                var line = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line).append("\n")
                    line = bufferedReader.readLine()
                }
                bufferedReader.close()

                val result = stringBuilder.toString()

                NotesLifecycle.importNotes(result)
                NotesLifecycle.saveNotes()
                loadNotes()
                Toast.makeText(this@MainActivity, getString(R.string.message_import) + "\n" + uri.path, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("shitty files", e.toString())
                Toast.makeText(this@MainActivity, getString(R.string.error_import), Toast.LENGTH_SHORT).show()
            }

        }
    }
    @ExperimentalStdlibApi
    private fun selectFileToImport() = selectFileToImportResult.launch("application/octet-stream")

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        settings = getSharedPreferences(Const.SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        NotesLifecycle.notes_save = getSharedPreferences(Const.NOTES_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        NotesLifecycle.loadNotes()

        setTheme(R.style.Theme_Notes)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        view = layoutInflater.inflate(R.layout.fragment_add_note_dialog_list_dialog, null)
        dialog = BottomSheetDialog(this@MainActivity)

        setListeners()
        loadNotes()
    }

    private fun loadNotes() {
        adapter = NoteAdapter(this@MainActivity)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)

        recyclerViewNotes.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    @ExperimentalStdlibApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_export -> {
                val filesDir: File = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
                if (!filesDir.exists()) {
                    if (filesDir.mkdirs()) {

                    }
                }
                val file = File(filesDir, "notes_backup.json")
                try {
                    if (!file.exists()) {
                        if (!file.createNewFile()) {
                            throw IOException("Cant able to create file")
                        }
                    }
                    val os: OutputStream = FileOutputStream(file)
                    val data: ByteArray = NotesLifecycle.exportNotes().toByteArray()
                    os.write(data)
                    os.close()
                    Log.e("TAG", "File Path= $file")
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, getString(R.string.error_export), Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this@MainActivity, getString(R.string.message_export) + "\n" + file, Toast.LENGTH_LONG).show()
            }
            R.id.action_import -> {
                selectFileToImport()
            }
            R.id.action_info -> {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Notes")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->

                    }
                    .show()
            }
        }

        return true
    }

    private fun setListeners() {
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        findViewById<RecyclerView>(R.id.recyclerViewNotes)
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            dialog.setContentView(view)
            dialog.show()

            view.findViewById<Button>(R.id.buttonAddNote).setOnClickListener {
                val themeText = view.findViewById<EditText>(R.id.editTextNoteTheme).text.toString()
                val contentText = view.findViewById<EditText>(R.id.editTextNoteContent).text.toString()

                NotesLifecycle.notes.add(0, Note(themeText, contentText))
                CoroutineScope(Dispatchers.IO).launch {
                    NotesLifecycle.saveNotes()
                }
                adapter.notifyItemInserted(0)

                dialog.cancel()
            }
            view.findViewById<Button>(R.id.buttonCancelAdding).setOnClickListener {
                dialog.cancel()
            }
        }
    }
}