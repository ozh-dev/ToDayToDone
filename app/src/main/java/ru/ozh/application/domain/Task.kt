package ru.ozh.application.domain

data class Task(
        val id: String,
        val text: String,
        val priority: Priority
)
