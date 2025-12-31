package com.example.ensuretrackapplication

class ClientModel (
    val id: String? =  null,          // Policy ID e.g. PL-2023-001-A
    val name: String? =  null,        // Client name
    val cnic: String? =  null,        // CNIC
    val phone: String? = null,       // Phone number
    val premium: String? = null,     // Premium e.g. PKR 15,000
    val dueDate: String? = null,     // Due Date e.g. 2024-07-15
    val plan: String? = null,        // Plan e.g. Health Max / Life Secure
    val status: String? = null,      // Status e.g. Due / Active
    val imageUrl: Int? = null

)