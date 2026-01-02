package com.example.ensuretrackapplication.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.Models.AvatarColor
import com.example.ensuretrackapplication.Models.Transaction
import com.example.ensuretrackapplication.Models.TransactionStatus
import com.example.ensuretrackapplication.R

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val onItemClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        private val tvClientName: TextView = itemView.findViewById(R.id.tvClientName)
        private val tvTransactionDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(transaction: Transaction) {
            // Set avatar initials
            val initials = transaction.clientName.split(" ")
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .take(2)
                .joinToString("")
            tvAvatar.text = initials

            // Set avatar background color
            val avatarBg = when (transaction.avatarColor) {
                AvatarColor.BLUE -> R.drawable.bg_avatar_blue
                AvatarColor.PURPLE -> R.drawable.bg_avatar_purple
                AvatarColor.RED -> R.drawable.bg_avatar_red
            }
            tvAvatar.setBackgroundResource(avatarBg)

            // Set avatar text color
            val avatarTextColor = when (transaction.avatarColor) {
                AvatarColor.BLUE -> "#2563EB"
                AvatarColor.PURPLE -> "#7C3AED"
                AvatarColor.RED -> "#DC2626"
            }
            tvAvatar.setTextColor(Color.parseColor(avatarTextColor))

            // Set client name and policy
            tvClientName.text = "${transaction.clientName} - ${transaction.policyType}"
            tvTransactionDate.text = transaction.date

            // Set amount with proper formatting
            val amountPrefix = if (transaction.status == TransactionStatus.SUCCESS) "+" else ""
            tvAmount.text = "$amountPrefix$${String.format("%.2f", transaction.amount)}"

            // Set status styling based on transaction status
            when (transaction.status) {
                TransactionStatus.SUCCESS -> {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.green)
                    )
                    tvStatus.text = "Success"
                    tvStatus.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.green)
                    )
                    tvStatus.setBackgroundResource(R.drawable.bg_status_success)
                }

                TransactionStatus.PENDING -> {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.text_primary)
                    )
                    tvStatus.text = "Pending"
                    tvStatus.setTextColor(Color.parseColor("#92400E"))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                }

                TransactionStatus.OVERDUE -> {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.text_primary)
                    )
                    tvStatus.text = "Overdue"
                    tvStatus.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.red)
                    )
                    tvStatus.setBackgroundResource(R.drawable.bg_status_overdue)
                }
            }

            // Set click listener
            itemView.setOnClickListener { onItemClick(transaction) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    // Optional: Method to update data
    fun updateTransactions(newTransactions: List<Transaction>) {
        // If using DiffUtil, implement here
        // For now, simple notifyDataSetChanged()
    }
}