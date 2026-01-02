package com.example.ensuretrackapplication.Models

data class Transaction(
    val id: String,
    val clientName: String,
    val policyType: String,
    val amount: Double,
    val date: String,
    val status: TransactionStatus,
    val avatarColor: AvatarColor
)
enum class TransactionStatus {
    SUCCESS,
    PENDING,
    OVERDUE
}

enum class AvatarColor {
    BLUE,
    PURPLE,
    RED
}
