package com.tm.thinknote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm.thinknote.db.NoteDatabase
import com.tm.thinknote.model.Note
import kotlinx.coroutines.launch

class HomeViewModel(noteDatabase: NoteDatabase) : ViewModel() {

    val dao = noteDatabase.noteDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    fun addNotes(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }
}