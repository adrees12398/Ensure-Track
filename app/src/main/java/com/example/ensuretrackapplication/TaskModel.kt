package com.example.ensuretrackapplication

data class Task(
    val title: String,
    val clientId: String,
    val dueDate: String,
    val colorResId: Int,
    var isCompleted: Boolean = false
)