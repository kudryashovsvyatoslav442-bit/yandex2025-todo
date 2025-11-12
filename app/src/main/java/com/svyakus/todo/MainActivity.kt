package com.svyakus.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jakewharton.threetenabp.AndroidThreeTen
import com.svyakus.todo.screen_list.NotesListScreen
import com.svyakus.todo.ui.theme.TodoYandexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        enableEdgeToEdge()
        setContent {
            TodoYandexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}