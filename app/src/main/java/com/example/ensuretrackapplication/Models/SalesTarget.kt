package com.example.ensuretrackapplication.Models

data class SalesTarget(
    val id: String = "",
    val agentId: String = "",
    val month: Int = 0,
    val year: Int = 0,
    val monthlyTarget: Double = 0.0,
    val quarterlyTarget: Double = 0.0,
    val yearlyTarget: Double = 0.0,
    val currentMonthlyProgress: Double = 0.0,
    val currentQuarterlyProgress: Double = 0.0,
    val currentYearlyProgress: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)