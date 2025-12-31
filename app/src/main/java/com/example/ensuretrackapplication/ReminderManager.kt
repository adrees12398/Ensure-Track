package com.example.ensuretrackapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class ReminderManager(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val CHANNEL_ID = "payment_reminders"
    private val CHANNEL_NAME = "Payment Reminders"
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    init {
        createNotificationChannel()
    }

    // Create notification channel for Android O and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for payment reminders and due dates"
                enableVibration(true)
                enableLights(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // Send immediate notification
    fun sendImmediateNotification(payment: Payment) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Payment Reminder: ${payment.clientName}")
            .setContentText("Amount: ₨${String.format("%,.0f", payment.amount)} | Due: ${dateFormat.format(payment.dueDate)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Payment reminder for ${payment.clientName}\n" +
                            "Policy: ${payment.policyNumber}\n" +
                            "Amount: ₨${String.format("%,.0f", payment.amount)}\n" +
                            "Due Date: ${dateFormat.format(payment.dueDate)}")
            )
            .build()

        try {
            notificationManager.notify(payment.id.hashCode(), notification)
        } catch (e: SecurityException) {
            // Handle permission denied
        }
    }

    // Schedule reminder for a payment (3 days before due date)
    fun scheduleReminder(payment: Payment) {
        val calendar = Calendar.getInstance()
        calendar.time = payment.dueDate
        calendar.add(Calendar.DAY_OF_YEAR, -3) // 3 days before due date

        // Only schedule if the reminder date is in the future
        if (calendar.time.after(Date())) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyCheckBroadcastReceiver::class.java).apply {
                putExtra("payment_id", payment.id)
                putExtra("client_name", payment.clientName)
                putExtra("amount", payment.amount)
                putExtra("due_date", payment.dueDate.time)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                payment.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    // Cancel scheduled reminder
    fun cancelReminder(paymentId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyCheckBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            paymentId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    // Schedule daily check for overdue payments
    fun scheduleDailyCheck() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyCheckBroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule for 9 AM every day
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // If it's past 9 AM today, schedule for tomorrow
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    // Send overdue notification
    fun sendOverdueNotification(payment: Payment) {
        val daysOverdue = Math.abs(payment.daysUntilDue())

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ Payment Overdue: ${payment.clientName}")
            .setContentText("$daysOverdue days overdue | Amount: ₨${String.format("%,.0f", payment.amount)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Payment is $daysOverdue days overdue!\n" +
                            "Client: ${payment.clientName}\n" +
                            "Policy: ${payment.policyNumber}\n" +
                            "Amount: ₨${String.format("%,.0f", payment.amount)}\n" +
                            "Due Date: ${dateFormat.format(payment.dueDate)}")
            )
            .build()

        try {
            notificationManager.notify(payment.id.hashCode(), notification)
        } catch (e: SecurityException) {
            // Handle permission denied
        }
    }
}