package com.awkitsune.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.widget.Toast





class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NoteAdapter
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java,
            "notes-database").build()
        setListeners()
        loadNotes()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun loadNotes() {
        val notesResult = CoroutineScope(Dispatchers.IO).async {
            ArrayList(db.noteDao.getAllNotes())
        }
        CoroutineScope(Dispatchers.IO).launch{
            NotesLifecycle.notes = notesResult.await()

            adapter = NoteAdapter(this@MainActivity)
            recyclerViewNotes = findViewById(R.id.recyclerViewNotes)

            recyclerViewNotes.post {
                recyclerViewNotes.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

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
            val view: View = layoutInflater.inflate(R.layout.fragment_add_note_dialog_list_dialog, null)
            val dialog = BottomSheetDialog(this@MainActivity)

            dialog.setContentView(view)
            dialog.show()

            view.findViewById<Button>(R.id.buttonAddNote).setOnClickListener {
                val themeText = view.findViewById<EditText>(R.id.editTextNoteTheme).text.toString()
                val contentText = view.findViewById<EditText>(R.id.editTextNoteContent).text.toString()

                NotesLifecycle.notes.add(0, Note(themeText, contentText))
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao.insertAll(NotesLifecycle.notes[0])
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