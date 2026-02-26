package com.tm.thinknote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm.thinknote.data.cache.DataStoreManager
import com.tm.thinknote.data.db.NoteDatabase
import com.tm.thinknote.data.remote.ApiService
import com.tm.thinknote.data.remote.HttpClientFactory
import com.tm.thinknote.data.remote.SyncRepository
import com.tm.thinknote.data.remote.SyncState
import com.tm.thinknote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    val noteDatabase: NoteDatabase,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    val dao = noteDatabase.noteDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    val userEmail = MutableStateFlow("")

    init {
        viewModelScope.launch {
            val email = dataStoreManager.getEmail()
            userEmail.value = email ?: ""
            performSync()
        }
    }

    fun performSync() {
        viewModelScope.launch {
            val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
            val userID = dataStoreManager.getUserId() ?: return@launch
            val syncRepository = SyncRepository(
                userID,
                noteDatabase.noteDao(),
                noteDatabase.syncMetadataDao(),
                apiService
            )

            syncRepository.performSync()

            syncRepository.syncState.collectLatest {
                when (it) {
                    is SyncState.Error -> {

                    }

                    is SyncState.Idle -> {

                    }

                    is SyncState.Success -> {

                    }

                    is SyncState.Syncing -> {

                    }
                }
            }
        }
    }

    fun addNotes(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
            performSync()
        }
    }
}