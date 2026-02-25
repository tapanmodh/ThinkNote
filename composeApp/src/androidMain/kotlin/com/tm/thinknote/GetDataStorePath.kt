package com.tm.thinknote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tm.thinknote.data.cache.createDataStore
import com.tm.thinknote.data.cache.dataStoreFileName

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)