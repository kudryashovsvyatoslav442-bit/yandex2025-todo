package com.svyakus.todo.navigation


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.svyakus.todo.screen_edit.EditScreen
import com.svyakus.todo.screen_list.TodoListScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val backStack = remember { mutableStateListOf<Any>(TodoList) }

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is TodoList -> NavEntry(key) {
                    TodoListScreen(
                        onItemClick = { todoUid ->
                            backStack.add(Edit(todoUid))
                        },
                        onCreate = { backStack.add(Create) },
                        modifier = modifier
                    )
                }

                is Create -> NavEntry(key) {
                    EditScreen(
                        todoUid = null,
                        onNavigateBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }

                is Edit -> NavEntry(key) {
                    EditScreen(
                        todoUid = key.uid,
                        onNavigateBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }

                else -> NavEntry(Unit) { Text("хз куда попали") }
            }
        }
    )
}