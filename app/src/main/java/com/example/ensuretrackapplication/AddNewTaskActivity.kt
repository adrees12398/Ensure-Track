package com.example.ensuretrackapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ensuretrackapplication.Fragments.TaskPriority
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewTaskActivity : AppCompatActivity() {

    // Views
    private lateinit var btnClose: ImageButton
    private lateinit var etTaskDescription: EditText
    private lateinit var etDueDate: EditText
    private lateinit var chipGroupPriority: ChipGroup
    private lateinit var chipUrgent: Chip
    private lateinit var chipHigh: Chip
    private lateinit var chipMedium: Chip
    private lateinit var chipLow: Chip
    private lateinit var btnSaveTask: Button

    // Data
    private var selectedPriority: TaskPriority = TaskPriority.URGENT
    private var selectedDate: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)
        initViews()
        setupListeners()
        setDefaultDate()
    }
    private fun initViews() {
        btnClose = findViewById(R.id.btnClose)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        etDueDate = findViewById(R.id.etDueDate)
        chipGroupPriority = findViewById(R.id.chipGroupPriority)
        chipUrgent = findViewById(R.id.chipUrgent)
        chipHigh = findViewById(R.id.chipHigh)
        chipMedium = findViewById(R.id.chipMedium)
        chipLow = findViewById(R.id.chipLow)
        btnSaveTask = findViewById(R.id.btnSaveTask)
    }

    private fun setupListeners() {
        // Close button
        btnClose.setOnClickListener {
            finish()
        }

        // Due date picker
        etDueDate.setOnClickListener {
            showDatePicker()
        }

        // Priority chips
        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipUrgent -> selectedPriority = TaskPriority.URGENT
                R.id.chipHigh -> selectedPriority = TaskPriority.HIGH
                R.id.chipMedium -> selectedPriority = TaskPriority.MEDIUM
                R.id.chipLow -> selectedPriority = TaskPriority.LOW
            }
        }

        // Save button
        btnSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun setDefaultDate() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        etDueDate.setText(dateFormat.format(selectedDate.time))
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                etDueDate.setText(dateFormat.format(selectedDate.time))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveTask() {
        val description = etTaskDescription.text.toString().trim()

        // Validation
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter task description", Toast.LENGTH_SHORT).show()
            etTaskDescription.error = "Task description is required"
            return
        }

        // TODO: Save task to database or pass back to previous activity
        // For now, just show success message
        Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show()

        // You can pass data back using Intent
        // val resultIntent = Intent()
        // resultIntent.putExtra("task_description", description)
        // resultIntent.putExtra("due_date", etDueDate.text.toString())
        // resultIntent.putExtra("priority", selectedPriority.name)
        // setResult(RESULT_OK, resultIntent)

        finish()
    }
}