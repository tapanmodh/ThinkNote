package com.tm.thinknote.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tm.thinknote.model.SyncMetadata

@Dao
interface SyncDataDao {

    @Query("SELECT * FROM syncmetadata WHERE id = 1")
    suspend fun getSyncMetadata(): SyncMetadata?

    @Query("UPDATE syncmetadata SET lastSyncTimestamp = :timestamp WHERE id = 1")
    suspend fun updateLastSyncTimestamp(timestamp: String)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetadata)

    @Query("UPDATE syncmetadata SET isSyncing = :isSyncing WHERE id = 1")
    suspend fun updateSyncingStatus(isSyncing: Boolean)
}