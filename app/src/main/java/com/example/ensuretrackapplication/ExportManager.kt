package com.example.ensuretrackapplication

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class ExportManager(private val context: Context) {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Export payments to CSV
    fun exportToCSV(payments: List<Payment>): Boolean {
        try {
            val fileName = "payments_${System.currentTimeMillis()}.csv"
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fileName
            )

            FileWriter(file).use { writer ->
                // Write header
                writer.append("Client Name,Policy Number,Amount,Due Date,Status,Paid Date,Notes\n")

                // Write data
                payments.forEach { payment ->
                    writer.append("${payment.clientName},")
                    writer.append("${payment.policyNumber},")
                    writer.append("${payment.amount},")
                    writer.append("${dateFormat.format(payment.dueDate)},")
                    writer.append("${payment.status},")
                    writer.append("${payment.paidDate?.let { dateFormat.format(it) } ?: "N/A"},")
                    writer.append("${payment.notes}\n")
                }
            }

            Toast.makeText(
                context,
                "Exported to: ${file.absolutePath}",
                Toast.LENGTH_LONG
            ).show()

            return true
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Export failed: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    // Generate monthly report
    fun generateMonthlyReport(payments: List<Payment>, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val monthPayments = payments.filter { payment ->
            val paymentCal = Calendar.getInstance()
            paymentCal.time = payment.dueDate
            paymentCal.get(Calendar.MONTH) == month &&
                    paymentCal.get(Calendar.YEAR) == year
        }

        val totalCollected = monthPayments
            .filter { it.status == PaymentStatus.PAID }
            .sumOf { it.amount }

        val totalPending = monthPayments
            .filter { it.status == PaymentStatus.PENDING }
            .sumOf { it.amount }

        val report = StringBuilder()
        report.append("Monthly Report - ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} $year\n\n")
        report.append("Total Payments: ${monthPayments.size}\n")
        report.append("Collected: ₨ ${String.format("%,.0f", totalCollected)}\n")
        report.append("Pending: ₨ ${String.format("%,.0f", totalPending)}\n")
        report.append("Collection Rate: ${if (monthPayments.isNotEmpty())
            String.format("%.1f", (totalCollected / (totalCollected + totalPending)) * 100) else "0"}%\n")

        return report.toString()
    }
}