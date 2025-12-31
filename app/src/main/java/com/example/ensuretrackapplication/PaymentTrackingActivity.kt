package com.example.ensuretrackapplication

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.R
import java.text.SimpleDateFormat
import java.util.*

class PaymentTrackingActivity : AppCompatActivity() {
    private lateinit var repository: PaymentRepository
    private lateinit var reminderManager: ReminderManager
    private lateinit var adapter: PaymentAdapter
    private lateinit var tvTotalCollected: TextView
    private lateinit var tvTotalPending: TextView
    private lateinit var tvOverdueCount: TextView
    private lateinit var rvPayments: RecyclerView
    private lateinit var btnAddPayment: Button
    private lateinit var btnFilterAll: Button
    private lateinit var btnFilterPending: Button
    private lateinit var btnFilterOverdue: Button
    private var currentFilter: PaymentStatus? = null
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Request notification permission for Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_tracking2)
        initializeViews()
        setupRepository()
        setupRecyclerView()
        setupClickListeners()
        requestNotificationPermission()

        // Schedule daily check
        reminderManager.scheduleDailyCheck()

        // Load and display data
        updateUI()
    }
    private fun initializeViews() {
        tvTotalCollected = findViewById(R.id.tvTotalCollected)
        tvTotalPending = findViewById(R.id.tvTotalPending)
        tvOverdueCount = findViewById(R.id.tvOverdueCount)
        rvPayments = findViewById(R.id.rvPayments)
        btnAddPayment = findViewById(R.id.btnAddPayment)
        btnFilterAll = findViewById(R.id.btnFilterAll)
        btnFilterPending = findViewById(R.id.btnFilterPending)
        btnFilterOverdue = findViewById(R.id.btnFilterOverdue)
    }

    private fun setupRepository() {
        repository = PaymentRepository(this)
        reminderManager = ReminderManager(this)

        // Update overdue statuses on app start
        repository.updateOverdueStatuses()
    }

    private fun setupRecyclerView() {
        adapter = PaymentAdapter(
            payments = listOf(),
            onMarkPaid = { payment -> markPaymentAsPaid(payment) },
            onSendReminder = { payment -> sendReminder(payment) }
        )

        rvPayments.layoutManager = LinearLayoutManager(this)
        rvPayments.adapter = adapter
    }

    private fun setupClickListeners() {
        btnAddPayment.setOnClickListener {
            showAddPaymentDialog()
        }

        btnFilterAll.setOnClickListener {
            filterPayments(null)
        }

        btnFilterPending.setOnClickListener {
            filterPayments(PaymentStatus.PENDING)
        }

        btnFilterOverdue.setOnClickListener {
            filterPayments(PaymentStatus.OVERDUE)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun updateUI() {
        // Update summary
        val summary = repository.getPaymentSummary()
        tvTotalCollected.text = "₨ ${String.format("%,.0f", summary.totalCollected)}"
        tvTotalPending.text = "₨ ${String.format("%,.0f", summary.totalPending)}"
        tvOverdueCount.text = summary.overdueCount.toString()

        // Update payments list
        filterPayments(currentFilter)
    }

    private fun filterPayments(status: PaymentStatus?) {
        currentFilter = status ?: currentFilter

        val allPayments = repository.loadPayments()
        val filteredPayments = when (status) {
            null -> allPayments
            PaymentStatus.OVERDUE -> allPayments.filter { it.isOverdue() }
            else -> allPayments.filter { it.status == status }
        }

        adapter.updateList(filteredPayments)
    }

    private fun showAddPaymentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_payment, null)
        val etClientName = dialogView.findViewById<EditText>(R.id.etClientName)
        val etPolicyNumber = dialogView.findViewById<EditText>(R.id.etPolicyNumber)
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val btnSelectDate = dialogView.findViewById<Button>(R.id.btnSelectDate)

        var selectedDate = Date()
        btnSelectDate.text = dateFormat.format(selectedDate)

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.time
                    btnSelectDate.text = dateFormat.format(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Add New Payment")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val clientName = etClientName.text.toString()
                val policyNumber = etPolicyNumber.text.toString()
                val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0

                if (clientName.isNotEmpty() && policyNumber.isNotEmpty() && amount > 0) {
                    val payment = Payment(
                        clientName = clientName,
                        policyNumber = policyNumber,
                        amount = amount,
                        dueDate = selectedDate
                    )

                    repository.addPayment(payment)
                    reminderManager.scheduleReminder(payment)

                    Toast.makeText(this, "Payment added successfully", Toast.LENGTH_SHORT).show()
                    updateUI()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun markPaymentAsPaid(payment: Payment) {
        AlertDialog.Builder(this)
            .setTitle("Mark as Paid")
            .setMessage("Mark payment for ${payment.clientName} as paid?")
            .setPositiveButton("Yes") { _, _ ->
                payment.status = PaymentStatus.PAID
                payment.paidDate = Date()
                repository.updatePayment(payment)
                reminderManager.cancelReminder(payment.id)

                Toast.makeText(this, "Payment marked as paid", Toast.LENGTH_SHORT).show()
                updateUI()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun sendReminder(payment: Payment) {
        reminderManager.sendImmediateNotification(payment)
        Toast.makeText(this, "Reminder sent to ${payment.clientName}", Toast.LENGTH_SHORT).show()

        // You can also add SMS/Email functionality here
        // sendSMSReminder(payment)
        // sendEmailReminder(payment)
    }
}