package com.example.focup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focup.ui.theme.FocupTheme

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as focupApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocupTheme { // Corrected Theme name
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val tasks by taskViewModel.allTasks.collectAsStateWithLifecycle()
                    TaskScreen(
                        tasks = tasks,
                        onAddTask = { title -> taskViewModel.addTask(title) },
                        onDeleteTask = { task -> taskViewModel.deleteTask(task) },
                        onUpdateTask = { task, isCompleted -> taskViewModel.updateTask(task, isCompleted) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    tasks: List<Task>,
    onAddTask: (String) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onUpdateTask: (Task, Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("focup To-Do") }, // Updated title
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("New Task") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            onAddTask(text)
                            text = ""
                        }
                    },
                    enabled = text.isNotBlank()
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onDelete = { onDeleteTask(task) },
                        onUpdate = { isCompleted -> onUpdateTask(task, isCompleted) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onUpdate: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onUpdate
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.title,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FocupTheme { // Corrected Theme name
        val sampleTasks = listOf(
            Task(1, "Buy milk", isCompleted = false),
            Task(2, "Walk the dog", isCompleted = true)
        )
        TaskScreen(tasks = sampleTasks, onAddTask = {}, onDeleteTask = {}, onUpdateTask = { _, _ -> })
    }
}