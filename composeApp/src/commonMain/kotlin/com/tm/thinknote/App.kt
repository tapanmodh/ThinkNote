package com.tm.thinknote

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tm.thinknote.db.NoteDatabase
import com.tm.thinknote.feature.auth.SignInScreen
import com.tm.thinknote.feature.auth.SignUpScreen
import com.tm.thinknote.feature.home.HomeScreen
import com.tm.thinknote.ui.theme.ThinkNoteAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(database: NoteDatabase) {

    ThinkNoteAppTheme {

        val navController = rememberNavController()

        NavHost(navController, startDestination = "home") {

            composable(route = "home") {
                HomeScreen(database, navController)
            }

            composable(route = "signup") {
                SignUpScreen(navController)
            }

            composable(route = "signin") {
                SignInScreen(navController)
            }
        }
    }
}