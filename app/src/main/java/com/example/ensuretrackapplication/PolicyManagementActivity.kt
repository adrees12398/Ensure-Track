package com.example.ensuretrackapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.databinding.ActivityPolicyManagementBinding

// ClientsAdapter.kt
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class PolicyManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPolicyManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}

