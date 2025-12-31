package com.example.ensuretrackapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PaymentAdapter(
    private var payments: List<Payment>,
    private val onMarkPaid: (Payment) -> Unit,
    private val onSendReminder: (Payment) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardPayment)
        val tvClientName: TextView = itemView.findViewById(R.id.tvClientName)
        val tvPolicyNumber: TextView = itemView.findViewById(R.id.tvPolicyNumber)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDaysInfo: TextView = itemView.findViewById(R.id.tvDaysInfo)
        val btnMarkPaid: Button = itemView.findViewById(R.id.btnMarkPaid)
        val btnReminder: Button = itemView.findViewById(R.id.btnReminder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]

        // Set basic info
        holder.tvClientName.text = payment.clientName
        holder.tvPolicyNumber.text = "Policy: ${payment.policyNumber}"
        holder.tvAmount.text = "₨ ${String.format("%,.0f", payment.amount)}"
        holder.tvDueDate.text = "Due: ${dateFormat.format(payment.dueDate)}"

        // Set status with color coding
        when (payment.status) {
            PaymentStatus.PAID -> {
                holder.tvStatus.text = "✓ PAID"
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E8F5E9"))
                holder.btnMarkPaid.visibility = View.GONE
                holder.btnReminder.visibility = View.GONE
                holder.tvDaysInfo.text = "Paid on: ${payment.paidDate?.let { dateFormat.format(it) } ?: "N/A"}"
                holder.tvDaysInfo.setTextColor(Color.parseColor("#4CAF50"))
            }
            PaymentStatus.OVERDUE -> {
                holder.tvStatus.text = "⚠ OVERDUE"
                holder.tvStatus.setTextColor(Color.parseColor("#F44336"))
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFEBEE"))
                holder.btnMarkPaid.visibility = View.VISIBLE
                holder.btnReminder.visibility = View.VISIBLE
                val daysOverdue = Math.abs(payment.daysUntilDue())
                holder.tvDaysInfo.text = "$daysOverdue days overdue"
                holder.tvDaysInfo.setTextColor(Color.parseColor("#F44336"))
            }
            PaymentStatus.PENDING -> {
                holder.tvStatus.text = "⏱ PENDING"
                holder.tvStatus.setTextColor(Color.parseColor("#FF9800"))
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
                holder.btnMarkPaid.visibility = View.VISIBLE
                holder.btnReminder.visibility = View.VISIBLE
                val daysUntilDue = payment.daysUntilDue()
                if (daysUntilDue >= 0) {
                    holder.tvDaysInfo.text = "Due in $daysUntilDue days"
                    holder.tvDaysInfo.setTextColor(Color.parseColor("#FF9800"))
                } else {
                    holder.tvDaysInfo.text = "${Math.abs(daysUntilDue)} days overdue"
                    holder.tvDaysInfo.setTextColor(Color.parseColor("#F44336"))
                }
            }
        }

        // Set button click listeners
        holder.btnMarkPaid.setOnClickListener {
            onMarkPaid(payment)
        }

        holder.btnReminder.setOnClickListener {
            onSendReminder(payment)
        }
    }

    override fun getItemCount(): Int = payments.size

    // Update the list
    fun updateList(newPayments: List<Payment>) {
        payments = newPayments
        notifyDataSetChanged()
    }
}