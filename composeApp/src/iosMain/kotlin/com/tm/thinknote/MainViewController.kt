package com.tm.thinknote

import androidx.compose.ui.window.ComposeUIViewController
import com.tm.thinknote.db.getNoteDatabase

fun MainViewController() = ComposeUIViewController { App(
    getNoteDatabase(getDatabaseBuilder())
) }