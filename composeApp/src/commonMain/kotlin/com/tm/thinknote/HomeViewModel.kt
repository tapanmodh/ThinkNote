package com.tm.thinknote

import androidx.lifecycle.ViewModel
import com.tm.thinknote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun addNotes(note: Note) {
        _notes.update { it + note }
    }
}