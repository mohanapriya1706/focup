package com.example.focup

import android.app.Application

class FocupApplication : Application() {
    // Using lazy so the database and repository are only created when they're needed
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}
