package com.example.ensuretrackapplication

import java.util.*

// Payment Status Enum
enum class PaymentStatus {
    PAID, PENDING, OVERDUE
}

// Payment Data Class
data class Payment(
    val id: String = UUID.randomUUID().toString(),
    val clientName: String,
    val policyNumber: String,
    val amount: Double,
    val dueDate: Date,
    var paidDate: Date? = null,
    var status: PaymentStatus = PaymentStatus.PENDING,
    val reminderSent: Boolean = false,
    val notes: String = ""
) {
    // Check if payment is overdue
    fun isOverdue(): Boolean {
        return status == PaymentStatus.PENDING && dueDate.before(Date())
    }

    // Days until due or overdue
    fun daysUntilDue(): Long {
        val diff = dueDate.time - Date().time
        return diff / (1000 * 60 * 60 * 24)
    }
}

// Summary Statistics
data class PaymentSummary(
    val totalCollected: Double,
    val totalPending: Double,
    val overdueCount: Int,
    val totalPayments: Int
)
