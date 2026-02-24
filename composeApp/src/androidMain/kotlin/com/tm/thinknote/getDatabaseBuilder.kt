package com.tm.thinknote

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tm.thinknote.db.NoteDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<NoteDatabase> {

    val appContext = context.applicationContext
    val dbPath = appContext.getDatabasePath("note_database.db")
    return Room.databaseBuilder(appContext, NoteDatabase::class.java, dbPath.path)
}