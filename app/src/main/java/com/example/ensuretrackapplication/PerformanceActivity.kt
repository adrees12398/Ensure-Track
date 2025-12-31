package com.example.ensuretrackapplication

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ensuretrackapplication.Models.CommissionRecord
import com.example.ensuretrackapplication.Models.SalesTarget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*
class PerformanceActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Views
    private lateinit var progressBar: ProgressBar
    private lateinit var tvMonthlyTarget: TextView
    private lateinit var tvMonthlyProgress: TextView
    private lateinit var progressMonthly: ProgressBar
    private lateinit var tvMonthlyPercentage: TextView
    private lateinit var tvMonthlyCommission: TextView

    private lateinit var tvQuarterlyTarget: TextView
    private lateinit var tvQuarterlyProgress: TextView
    private lateinit var progressQuarterly: ProgressBar
    private lateinit var tvQuarterlyPercentage: TextView
    private lateinit var tvQuarterlyCommission: TextView

    private lateinit var tvYearlyTarget: TextView
    private lateinit var tvYearlyProgress: TextView
    private lateinit var progressYearly: ProgressBar
    private lateinit var tvYearlyPercentage: TextView
    private lateinit var tvYearlyCommission: TextView

    private lateinit var btnSetTarget: Button
    private lateinit var btnAddCommission: Button

    private var currentAgentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        currentAgentId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (auth.currentUser == null) {
            Toast.makeText(this, "Please login first!", Toast.LENGTH_LONG).show()

            // Ya to activity band kar do
            finish()

            // Ya login screen pe le jao (better hai)
            // startActivity(Intent(this, LoginActivity::class.java))
            // finish()

            return   // ← bohot zaroori! baqi code mat chalaao
        }

        // Ab yahan tak aaya matlab user login hai
        currentAgentId = auth.currentUser!!.uid   // safe hai ab

        initViews()
        setupClickListeners()
        loadPerformanceData()
        initViews()
        setupClickListeners()
        loadPerformanceData()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)

        tvMonthlyTarget = findViewById(R.id.tvMonthlyTarget)
        tvMonthlyProgress = findViewById(R.id.tvMonthlyProgress)
        progressMonthly = findViewById(R.id.progressMonthly)
        tvMonthlyPercentage = findViewById(R.id.tvMonthlyPercentage)
        tvMonthlyCommission = findViewById(R.id.tvMonthlyCommission)

        tvQuarterlyTarget = findViewById(R.id.tvQuarterlyTarget)
        tvQuarterlyProgress = findViewById(R.id.tvQuarterlyProgress)
        progressQuarterly = findViewById(R.id.progressQuarterly)
        tvQuarterlyPercentage = findViewById(R.id.tvQuarterlyPercentage)
        tvQuarterlyCommission = findViewById(R.id.tvQuarterlyCommission)

        tvYearlyTarget = findViewById(R.id.tvYearlyTarget)
        tvYearlyProgress = findViewById(R.id.tvYearlyProgress)
        progressYearly = findViewById(R.id.progressYearly)
        tvYearlyPercentage = findViewById(R.id.tvYearlyPercentage)
        tvYearlyCommission = findViewById(R.id.tvYearlyCommission)

        btnSetTarget = findViewById(R.id.btnSetTarget)
        btnAddCommission = findViewById(R.id.btnAddCommission)
    }

    private fun setupClickListeners() {
        btnSetTarget.setOnClickListener { showSetTargetDialog() }
        btnAddCommission.setOnClickListener { showAddCommissionDialog() }
    }

    private fun loadPerformanceData() {
        progressBar.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        db.collection("sales_targets")
            .whereEqualTo("agentId", currentAgentId)
            .whereEqualTo("month", currentMonth)
            .whereEqualTo("year", currentYear)
            .limit(1)
            .get()
            .addOnSuccessListener { targetDocs ->
                val target = if (targetDocs.isEmpty) {
                    SalesTarget() // default empty target
                } else {
                    targetDocs.documents.first().toObject(SalesTarget::class.java) ?: SalesTarget()
                }
                updateUIWithTarget(target)
                loadCommissions(currentMonth, currentYear)
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load target: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadCommissions(currentMonth: Int, currentYear: Int) {
        // Helper function to safely get commission
        fun DocumentSnapshot.getCommission(): Double =
            toObject(CommissionRecord::class.java)?.commissionEarned ?: 0.0

        // Monthly
        db.collection("commissions")
            .whereEqualTo("agentId", currentAgentId)
            .whereEqualTo("month", currentMonth)
            .whereEqualTo("year", currentYear)
            .get()
            .addOnSuccessListener { docs ->
                val total = docs.documents.sumOf { it.getCommission() }
                tvMonthlyCommission.text = "Commission: ${formatCurrency(total)}"
            }

        // Quarterly
        val currentQuarter = (currentMonth - 1) / 3 + 1
        val quarterStart = (currentQuarter - 1) * 3 + 1
        val quarterEnd = quarterStart + 2

        db.collection("commissions")
            .whereEqualTo("agentId", currentAgentId)
            .whereEqualTo("year", currentYear)
            .whereGreaterThanOrEqualTo("month", quarterStart)
            .whereLessThanOrEqualTo("month", quarterEnd)
            .get()
            .addOnSuccessListener { docs ->
                val total = docs.documents.sumOf { it.getCommission() }
                tvQuarterlyCommission.text = "Commission: ${formatCurrency(total)}"
            }

        // Yearly
        db.collection("commissions")
            .whereEqualTo("agentId", currentAgentId)
            .whereEqualTo("year", currentYear)
            .get()
            .addOnSuccessListener { docs ->
                val total = docs.documents.sumOf { it.getCommission() }
                tvYearlyCommission.text = "Total Commission: ${formatCurrency(total)}"
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
            }
    }

    private fun updateUIWithTarget(target: SalesTarget) {
        // Monthly
        tvMonthlyTarget.text = formatCurrency(target.monthlyTarget)
        tvMonthlyProgress.text = formatCurrency(target.currentMonthlyProgress)
        val monthlyPct = if (target.monthlyTarget > 0) {
            (target.currentMonthlyProgress / target.monthlyTarget) * 100
        } else 0.0
        progressMonthly.progress = monthlyPct.toInt().coerceIn(0, 100)
        tvMonthlyPercentage.text = "${String.format("%.1f", monthlyPct)}%"

        // Quarterly
        tvQuarterlyTarget.text = formatCurrency(target.quarterlyTarget)
        tvQuarterlyProgress.text = formatCurrency(target.currentQuarterlyProgress)
        val quarterlyPct = if (target.quarterlyTarget > 0) {
            (target.currentQuarterlyProgress / target.quarterlyTarget) * 100
        } else 0.0
        progressQuarterly.progress = quarterlyPct.toInt().coerceIn(0, 100)
        tvQuarterlyPercentage.text = "${String.format("%.1f", quarterlyPct)}%"

        // Yearly
        tvYearlyTarget.text = formatCurrency(target.yearlyTarget)
        tvYearlyProgress.text = formatCurrency(target.currentYearlyProgress)
        val yearlyPct = if (target.yearlyTarget > 0) {
            (target.currentYearlyProgress / target.yearlyTarget) * 100
        } else 0.0
        progressYearly.progress = yearlyPct.toInt().coerceIn(0, 100)
        tvYearlyPercentage.text = "${String.format("%.1f", yearlyPct)}%"
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "PK")).format(amount)
    }

    private fun showSetTargetDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_set_target)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val etMonthly = dialog.findViewById<EditText>(R.id.etMonthlyTarget)
        val etQuarterly = dialog.findViewById<EditText>(R.id.etQuarterlyTarget)
        val etYearly = dialog.findViewById<EditText>(R.id.etYearlyTarget)
        val btnSave = dialog.findViewById<Button>(R.id.btnSaveTarget)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val monthly = etMonthly.text.toString().trim().toDoubleOrNull()
            val quarterly = etQuarterly.text.toString().trim().toDoubleOrNull()
            val yearly = etYearly.text.toString().trim().toDoubleOrNull()

            if (monthly != null && quarterly != null && yearly != null) {
                if (monthly >= 0 && quarterly >= 0 && yearly >= 0) {
                    saveTarget(monthly, quarterly, yearly)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Targets cannot be negative", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun saveTarget(monthly: Double, quarterly: Double, yearly: Double) {
        progressBar.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        val target = SalesTarget(
            id = UUID.randomUUID().toString(),
            agentId = currentAgentId,
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR),
            monthlyTarget = monthly,
            quarterlyTarget = quarterly,
            yearlyTarget = yearly,
            currentMonthlyProgress = 0.0,
            currentQuarterlyProgress = 0.0,
            currentYearlyProgress = 0.0
        )

        db.collection("sales_targets")
            .document(target.id)
            .set(target)
            .addOnSuccessListener {
                Toast.makeText(this, "Target saved successfully!", Toast.LENGTH_SHORT).show()
                loadPerformanceData()
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to save target: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun showAddCommissionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_commission)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val etPolicyNumber = dialog.findViewById<EditText>(R.id.etPolicyNumber)
        val etClientName = dialog.findViewById<EditText>(R.id.etClientName)
        val etPremiumAmount = dialog.findViewById<EditText>(R.id.etPremiumAmount)
        val etCommissionRate = dialog.findViewById<EditText>(R.id.etCommissionRate)
        val btnSave = dialog.findViewById<Button>(R.id.btnSaveCommission)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelCommission)

        btnSave.setOnClickListener {
            val policy = etPolicyNumber.text.toString().trim()
            val client = etClientName.text.toString().trim()
            val premium = etPremiumAmount.text.toString().trim().toDoubleOrNull()
            val rate = etCommissionRate.text.toString().trim().toDoubleOrNull()

            if (policy.isNotEmpty() && client.isNotEmpty() && premium != null && rate != null) {
                if (premium > 0 && rate in 0.0..100.0) {
                    saveCommission(policy, client, premium, rate)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Invalid premium or rate", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun saveCommission(
        policyNumber: String,
        clientName: String,
        premium: Double,
        rate: Double
    ) {
        progressBar.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        val commissionEarned = premium * (rate / 100)

        val commission = CommissionRecord(
            id = UUID.randomUUID().toString(),
            agentId = currentAgentId,
            policyNumber = policyNumber,
            clientName = clientName,
            premiumAmount = premium,
            commissionRate = rate,
            commissionEarned = commissionEarned,
            collectionDate = System.currentTimeMillis(),
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR)
        )

        db.collection("commissions")
            .document(commission.id)
            .set(commission)
            .addOnSuccessListener {
                updateProgress(premium)
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to save commission: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateProgress(amount: Double) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        db.collection("sales_targets")
            .whereEqualTo("agentId", currentAgentId)
            .whereEqualTo("month", currentMonth)
            .whereEqualTo("year", currentYear)
            .limit(1)
            .get()
            .addOnSuccessListener { docs ->
                if (docs.isEmpty) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "No target found. Please set target first.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                val doc = docs.documents.first()
                val docId = doc.id
                val target = doc.toObject(SalesTarget::class.java) ?: return@addOnSuccessListener

                val updates = hashMapOf<String, Any>(
                    "currentMonthlyProgress" to (target.currentMonthlyProgress + amount),
                    "currentQuarterlyProgress" to (target.currentQuarterlyProgress + amount),
                    "currentYearlyProgress" to (target.currentYearlyProgress + amount)
                )

                db.collection("sales_targets")
                    .document(docId)
                    .update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Commission added & progress updated!", Toast.LENGTH_SHORT).show()
                        loadPerformanceData()
                    }
                    .addOnFailureListener { e ->
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Failed to update progress: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
            }
    }
}