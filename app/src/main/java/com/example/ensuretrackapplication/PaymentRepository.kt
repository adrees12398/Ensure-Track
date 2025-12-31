package com.example.ensuretrackapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class PaymentRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("PaymentPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val PAYMENTS_KEY = "payments_list"

    // Save payments to SharedPreferences
    private fun savePayments(payments: List<Payment>) {
        val json = gson.toJson(payments)
        sharedPreferences.edit().putString(PAYMENTS_KEY, json).apply()
    }

    // Load payments from SharedPreferences
    fun loadPayments(): List<Payment> {
        val json = sharedPreferences.getString(PAYMENTS_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Payment>>() {}.type
        return gson.fromJson(json, type)
    }

    // Add new payment
    fun addPayment(payment: Payment) {
        val payments = loadPayments().toMutableList()
        payments.add(payment)
        savePayments(payments)
    }

    // Update existing payment
    fun updatePayment(payment: Payment) {
        val payments = loadPayments().toMutableList()
        val index = payments.indexOfFirst { it.id == payment.id }
        if (index != -1) {
            payments[index] = payment
            savePayments(payments)
        }
    }

    // Delete payment
    fun deletePayment(paymentId: String) {
        val payments = loadPayments().toMutableList()
        payments.removeAll { it.id == paymentId }
        savePayments(payments)
    }

    // Get payment by ID
    fun getPaymentById(paymentId: String): Payment? {
        return loadPayments().find { it.id == paymentId }
    }

    // Update overdue statuses
    fun updateOverdueStatuses() {
        val payments = loadPayments().toMutableList()
        var updated = false

        payments.forEachIndexed { index, payment ->
            if (payment.status == PaymentStatus.PENDING && payment.dueDate.before(Date())) {
                payments[index] = payment.copy(status = PaymentStatus.OVERDUE)
                updated = true
            }
        }

        if (updated) {
            savePayments(payments)
        }
    }

    // Get payment summary
    fun getPaymentSummary(): PaymentSummary {
        val payments = loadPayments()

        val totalCollected = payments
            .filter { it.status == PaymentStatus.PAID }
            .sumOf { it.amount }

        val totalPending = payments
            .filter { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.OVERDUE }
            .sumOf { it.amount }

        val overdueCount = payments.count { it.status == PaymentStatus.OVERDUE }

        return PaymentSummary(
            totalCollected = totalCollected,
            totalPending = totalPending,
            overdueCount = overdueCount,
            totalPayments = payments.size
        )
    }

    // Get payments by status
    fun getPaymentsByStatus(status: PaymentStatus): List<Payment> {
        return loadPayments().filter { it.status == status }
    }

    // Get overdue payments
    fun getOverduePayments(): List<Payment> {
        return loadPayments().filter { it.isOverdue() }
    }

    // Get payments due soon (within next 7 days)
    fun getPaymentsDueSoon(): List<Payment> {
        val sevenDaysFromNow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
        }.time

        return loadPayments().filter { payment ->
            payment.status == PaymentStatus.PENDING &&
                    payment.dueDate.after(Date()) &&
                    payment.dueDate.before(sevenDaysFromNow)
        }
    }

    // Clear all payments (for testing/reset)
    fun clearAllPayments() {
        sharedPreferences.edit().remove(PAYMENTS_KEY).apply()
    }
}