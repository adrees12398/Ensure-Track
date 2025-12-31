package com.example.ensuretrackapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DailyCheckBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val repository = PaymentRepository(context)
        val reminderManager = ReminderManager(context)

        // Update overdue statuses
        repository.updateOverdueStatuses()

        // Send notifications for overdue payments
        repository.getOverduePayments().forEach { payment ->
            reminderManager.sendOverdueNotification(payment)
        }
    }
}