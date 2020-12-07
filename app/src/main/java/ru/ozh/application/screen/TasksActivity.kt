package ru.ozh.application.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import ru.ozh.application.R
import ru.ozh.application.view.CapsuleView
import ru.ozh.application.view.ColorChooseView

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        val addBtn = findViewById<Button>(R.id.add_task_btn)
        val openPriorityBtn = findViewById<CapsuleView>(R.id.open_priority_btn)
        val colorPicker = findViewById<ColorChooseView>(R.id.color_choose_view)

        addBtn.setOnClickListener {
            colorPicker.hide()
        }

        openPriorityBtn.setOnClickListener {
            colorPicker.show()
        }
    }
}