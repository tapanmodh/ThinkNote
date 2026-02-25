package com.tm.thinknote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm.thinknote.data.cache.DataStoreManager
import com.tm.thinknote.data.db.NoteDatabase
import com.tm.thinknote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(noteDatabase: NoteDatabase, dataStoreManager: DataStoreManager) : ViewModel() {

    val dao = noteDatabase.noteDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    val userEmail = MutableStateFlow("")

    init {
        viewModelScope.launch {
            val email = dataStoreManager.getEmail()
            userEmail.value = email ?: ""
        }
    }

    fun addNotes(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }
}