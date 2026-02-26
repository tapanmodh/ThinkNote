package com.tm.thinknote.data.remote

import com.tm.thinknote.data.db.NoteDao
import com.tm.thinknote.data.db.SyncDataDao
import com.tm.thinknote.model.Note
import com.tm.thinknote.model.NoteChange
import com.tm.thinknote.model.SyncRequest
import com.tm.thinknote.model.SyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SyncRepository(
    private val userID: String,
    private val noteDao: NoteDao,
    private val syncDataDao: SyncDataDao,
    private val apiService: ApiService
) {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState.asStateFlow()

    suspend fun performSync() = withContext(Dispatchers.IO) {

        try {
            val metadata = syncDataDao.getSyncMetadata()
            if (metadata?.isSyncing == true) {
                return@withContext
            }

            _syncState.value = SyncState.Syncing
            syncDataDao.updateSyncingStatus(true)

            val dirtyNotes = noteDao.getDirtyNotes()

            val syncRequest = SyncRequest(
                since = metadata?.lastSyncTimeStamp,
                changes = dirtyNotes.map { note ->
                    NoteChange(
                        id = note.id,
                        title = note.title,
                        body = note.description,
                        isDeleted = note.isDeleted,
                        updatedAt = note.updatedAt
                    )
                }
            )

            val response = apiService.sync(syncRequest)
            response.getOrNull()?.let {
                processSyncResponse(it)
            }
            syncDataDao.updateLastSyncTimestamp(response.getOrNull()?.nextSince ?: "")
            syncDataDao.updateSyncingStatus(false)

            _syncState.value = SyncState.Success(response.getOrNull()!!)
        } catch (ex: Exception) {
            _syncState.value = SyncState.Error(ex.message ?: "An error occurred")
        }
    }

    suspend fun processSyncResponse(response: SyncResponse) = withContext(Dispatchers.IO) {

        if (response.applied.isNotEmpty()) {
            noteDao.markAsSynced(response.applied)
        }

        if (response.conflicts.isNotEmpty()) {
            val conflictNotes = response.conflicts.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(conflictNotes)
        }

        if (response.changes.isNotEmpty()) {
            val serverNotes = response.changes.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(serverNotes)
        }
    }
}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val data: SyncResponse) : SyncState()
    data class Error(val error: String) : SyncState()
}