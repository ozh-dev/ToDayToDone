package ru.ozh.application.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.ozh.application.R

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
    }
}