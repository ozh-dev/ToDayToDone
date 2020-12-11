package ru.ozh.application.domain

enum class Priority(val key: Int) {
    LOW(0),
    MIDDLE(1),
    HIGH(2),
    UNKNOWN(-1)
}