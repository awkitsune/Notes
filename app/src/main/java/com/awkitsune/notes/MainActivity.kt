package com.awkitsune.notes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.awkitsune.notes.NotesLifecycle.Companion.notes
import com.awkitsune.notes.Util.Companion.checkBody
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.io.*
import java.security.MessageDigest
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val APP_SIGNATURE = "1038C0E34658923C4192E61B16846"

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
                val stringBuilder = StringBuilder()
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
                Log.d("totally not a secret key uwu", Util.getSHA1("arctf".toByteArray() + e.toString().toByteArray()))
                Log.e("shitty files", e.toString())
                Toast.makeText(this@MainActivity, getString(R.string.error_import), Toast.LENGTH_SHORT).show()
            }

        }
    }
    @ExperimentalStdlibApi
    private fun selectFileToImport() = selectFileToImportResult.launch("application/octet-stream")

    @SuppressLint("PackageManagerGetSignatures")
    @Throws(NameNotFoundException::class)
    fun validateAppSignature(context: Context): Boolean {
        val packageInfo = context.packageManager.getPackageInfo(
            packageName, PackageManager.GET_SIGNATURES
        )
        //note sample just checks the first signature
        for (signature in packageInfo.signatures) {
            // SHA1 the signature
            val sha1: String = getSHA1(signature.toByteArray())
            // check is matches hardcoded value
            return APP_SIGNATURE == sha1
        }
        return false
    }

    private fun getSHA1(sig: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA1")
        digest.update(sig)
        val hashtext = digest.digest()
        return Util.bytesToHex(hashtext)
    }


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        if (validateAppSignature(this.applicationContext)) {
            Log.d("vj8bHtU1YuIHK8u6aLB5yIRHgGlpyAacDQ1BYuzYQpstcLxf3J2Eu1eLd0ra8e2A", "{}")
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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

                NotesLifecycle.notes.add(0, Note(themeText, contentText, getSig(contentText)))
                Log.d("dbg", NotesLifecycle.notes[0].figment.length.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    NotesLifecycle.saveNotes()
                }
                adapter.notifyItemInserted(0)

                view.findViewById<EditText>(R.id.editTextNoteTheme).setText("")
                view.findViewById<EditText>(R.id.editTextNoteContent).setText("")

                dialog.cancel()
            }
            view.findViewById<Button>(R.id.buttonCancelAdding).setOnClickListener {
                dialog.cancel()
            }
        }
    }

    private fun getSig(contentText: String): String {
        if (contentText == "gimmeSomeCode") {
            return "arc" + Util.bytesToHex(contentText.toByteArray())
        }
        val footer = getString(R.string.key_header).substring(0, 5)
        val body = Util.bytesToHex(ByteArray(0))

        if(!checkBody(body)) {
            val akeyforctf = footer + "{" + Util.bytesToHex(ByteArray(0)) + "}"
            return akeyforctf + ""
        }

        val sheesh = body.toByteArray()

        return Util.bytesToHex(getSHA1(sheesh).toByteArray())

        return sheesh.toString()
    }
}