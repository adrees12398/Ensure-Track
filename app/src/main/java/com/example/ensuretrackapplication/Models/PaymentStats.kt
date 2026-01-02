package com.example.ensuretrackapplication.Models

data class PaymentStats(
    val totalRevenue: Double,
    val targetRevenue: Double,
    val collectedAmount: Double,
    val collectedCount: Int,
    val pendingAmount: Double,
    val pendingCount: Int,
    val overdueAmount: Double,
    val overdueCount: Int
) {
    val progressPercentage: Int
        get() = ((totalRevenue / targetRevenue) * 100).toInt()

    val remainingAmount: Double
        get() = targetRevenue - totalRevenue
}