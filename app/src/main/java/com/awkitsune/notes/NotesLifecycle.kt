package com.awkitsune.notes

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.*
import com.google.gson.GsonBuilder
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class NotesLifecycle {
    companion object{
        private val gson = GsonBuilder().create()
        lateinit var notes_save: SharedPreferences

        var notes = ArrayList<Note>()

        @ExperimentalStdlibApi
        fun loadNotes() {
            try {
                notes = gson.fromJson(
                    notes_save.getString(Const.NOTES_SHARED_PREFERENCES, ""),
                    typeOf<ArrayList<Note>>().javaType
                )!!
            } catch (exception: Exception) {
                Log.d("notes loading", exception.toString())
            }
        }
        fun saveNotes() {
            notes_save.edit()
                .putString(Const.NOTES_SHARED_PREFERENCES, Gson().toJson(notes))
                .apply()
        }

        fun exportNotes(): String {
            var text = ""
            try {
                text = gson.toJson(notes)
            } catch (ex: Exception) {
                Log.d("export error", ex.toString())
            }
            return text
        }
        @ExperimentalStdlibApi
        fun importNotes(json: String): Boolean {
            try {
                var temp = gson.fromJson<ArrayList<Note>>(json, typeOf<ArrayList<Note>>().javaType)
                notes.addAll(temp)
                return true
            } catch (ex: Exception) {
                Log.d("export error", ex.toString())
            }
            return false
        }
    }
}