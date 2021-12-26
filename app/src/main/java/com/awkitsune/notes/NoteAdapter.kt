package com.awkitsune.notes

import android.content.Context;
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*
import androidx.recyclerview.widget.RecyclerView;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteAdapter(context: Context): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note: Note = NotesLifecycle.notes[position]

        holder.themeView.text = note.theme
        holder.dateView.text = note.date
        holder.contentView.text = note.content

        holder.itemView.setOnClickListener {
            val additions = holder.itemView.findViewById<LinearLayout>(R.id.itemAction)

            when(additions.visibility){
                View.GONE -> additions.visibility = View.VISIBLE
                View.VISIBLE -> additions.visibility = View.GONE
                View.INVISIBLE -> additions.visibility = View.VISIBLE
            }
        }
        holder.itemView.findViewById<Button>(R.id.buttonDeleteItem).setOnClickListener {
            NotesLifecycle.notes.removeAt(position)
            this@NoteAdapter.notifyItemRemoved(position)
            CoroutineScope(Dispatchers.IO).launch {
                NotesLifecycle.saveNotes()
            }
        }
    }

    override fun getItemCount(): Int {
        return  NotesLifecycle.notes.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val themeView: TextView = itemView.findViewById(R.id.textViewTheme)
        val dateView: TextView = itemView.findViewById(R.id.textViewDate)
        val contentView: TextView = itemView.findViewById(R.id.textViewNote)
    }
}