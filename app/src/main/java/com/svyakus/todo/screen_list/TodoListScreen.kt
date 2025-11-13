package com.svyakus.todo.screen_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.svyakus.todo.model.Importance
import com.svyakus.todo.model.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    onItemClick: (uid: String) -> Unit,
    onCreate: () -> Unit,
    vm: TodoListViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.dispatch(TodoListIntent.Load)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(Icons.Default.Add, contentDescription = "Создать")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (state.loading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (state.items.isEmpty()) {
                Text("Нет задач", Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    items(state.items, key = { it.uid }) { item ->
                        ListItemCard(
                            item = item,
                            onClick = { onItemClick(item.uid) },
                            onDelete = { vm.dispatch(TodoListIntent.Delete(item.uid)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItemCard(
    item: TodoItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.4f },
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить"
                )
            }
        },
        content = {
            Card(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp, horizontal = 3.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(40.dp)
                            .background(Color(item.color), shape = RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (item.importance) {
                            Importance.HIGH -> Text("❗", color = Color.Red)
                            Importance.NORMAL -> Text("🙏", color = Color.Gray)
                            Importance.LOW -> Text("😴", color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (item.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.deadline != null) {
                                Text(
                                    text = item.deadline.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(10.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = item.isDone,
                        onCheckedChange = null,
                        enabled = false
                    )
                }
            }

        }
    )
}
