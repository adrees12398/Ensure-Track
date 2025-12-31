package com.example.ensuretrackapplication.Models

data class CommissionRecord(
    val id: String = "",
    val agentId: String = "",
    val policyNumber: String = "",
    val clientName: String = "",
    val premiumAmount: Double = 0.0,
    val commissionRate: Double = 0.0,
    val commissionEarned: Double = 0.0,
    val collectionDate: Long = System.currentTimeMillis(),
    val month: Int = 0,
    val year: Int = 0
)