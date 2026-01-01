package com.example.ensuretrackapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.ensuretrackapplication.databinding.ActivityAddNewClientBinding
import java.util.Calendar

class AddNewClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewClientBinding


    private val policyTypes = arrayOf(
        "Life Insurance",
        "Health Insurance",
        "Auto Insurance",
        "Property Insurance"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sabse important: Binding ko initialize karo
        binding = ActivityAddNewClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        // Policy Type Dropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, policyTypes)
        binding.etPolicyType.setAdapter(adapter)

        // Due Date Picker
        binding.etDueDate.setOnClickListener {
            showDatePicker()
        }

        // Save Button Click
        binding.btnSaveClient.setOnClickListener {
            saveClient()
        }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDueDate.setText(formattedDate)
            },
            year, month, day
        )
        datePicker.show()
    }

    private fun saveClient() {
        val fullName = binding.etFullName.text.toString().trim()
        val cnic = binding.etCnic.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val policyNumber = binding.etPolicyNumber.text.toString().trim()
        val policyType = binding.etPolicyType.text.toString()
        val premium = binding.etPremiumAmount.text.toString().trim()
        val dueDate = binding.etDueDate.text.toString()

        // Basic validation
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Name is required"
            return
        }

        // TODO: Save to database / ViewModel / Firestore etc.
        // Example: show success toast
        // Toast.makeText(this, "Client saved: $fullName", Toast.LENGTH_SHORT).show()

        finish() // Wapas jaao
    }
}