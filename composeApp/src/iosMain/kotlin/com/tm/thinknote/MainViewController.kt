package com.tm.thinknote

import androidx.compose.ui.window.ComposeUIViewController
import com.tm.thinknote.data.cache.DataStoreManager
import com.tm.thinknote.data.db.getNoteDatabase
import com.tm.thinknote.data.remote.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        getNoteDatabase(getDatabaseBuilder()), DataStoreManager(createDataStore())
    )
}