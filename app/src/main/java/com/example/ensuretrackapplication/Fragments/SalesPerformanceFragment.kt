package com.example.ensuretrackapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ensuretrackapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SalesPerformanceFragment : Fragment() {

    // Views declare karte hain
    private lateinit var btnMonthly: Button
    private lateinit var btnQuarterly: Button
    private lateinit var btnYearly: Button
    private lateinit var tvCurrentAmount: TextView
    private lateinit var tvTotalEarned: TextView
    private lateinit var progressCircle: ProgressBar
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // PEHLE layout inflate karo
        val view = inflater.inflate(R.layout.fragment_sales_performance, container, false)

        // PHIR views ko initialize karo (inflated view se)
        initViews(view)

        // PHIR button listeners setup karo
        setupButtonListeners()

        // PHIR FAB listener setup karo
        setupFABListener()

        // View return karo
        return view
    }

    // Saare views ko find karte hain - VIEW parameter chahiye
    private fun initViews(view: View) {
        btnMonthly = view.findViewById(R.id.btnMonthly)
        btnQuarterly = view.findViewById(R.id.btnQuarterly)
        btnYearly = view.findViewById(R.id.btnYearly)
        tvCurrentAmount = view.findViewById(R.id.tvCurrentAmount)
        tvTotalEarned = view.findViewById(R.id.tvTotalEarned)
        progressCircle = view.findViewById(R.id.progressCircle)
        fabAdd = view.findViewById(R.id.fabAdd)
    }

    // Button listeners setup karte hain
    private fun setupButtonListeners() {
        // Monthly button click
        btnMonthly.setOnClickListener {
            updateTimeframeUI("Monthly")
            updateData(
                currentAmount = "$8.5k",
                totalEarned = "$1,250.00",
                progress = 85
            )
            Toast.makeText(requireContext(), "Monthly view selected", Toast.LENGTH_SHORT).show()
        }

        // Quarterly button click
        btnQuarterly.setOnClickListener {
            updateTimeframeUI("Quarterly")
            updateData(
                currentAmount = "$24k",
                totalEarned = "$3,500.00",
                progress = 80
            )
            Toast.makeText(requireContext(), "Quarterly view selected", Toast.LENGTH_SHORT).show()
        }

        // Yearly button click
        btnYearly.setOnClickListener {
            updateTimeframeUI("Yearly")
            updateData(
                currentAmount = "$95k",
                totalEarned = "$12,800.00",
                progress = 79
            )
            Toast.makeText(requireContext(), "Yearly view selected", Toast.LENGTH_SHORT).show()
        }
    }

    // FAB (Floating Action Button) listener
    private fun setupFABListener() {
        fabAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Add New Sale", Toast.LENGTH_SHORT).show()
            // Yahan aap naya activity open kar sakte hain
            // startActivity(Intent(requireContext(), AddSaleActivity::class.java))
        }
    }

    // Button selection ko update karte hain
    private fun updateTimeframeUI(selected: String) {
        // Pehle sabhi buttons ko normal color karo
        btnMonthly.apply {
            setBackgroundResource(android.R.color.transparent)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        }
        btnQuarterly.apply {
            setBackgroundResource(android.R.color.transparent)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        }
        btnYearly.apply {
            setBackgroundResource(android.R.color.transparent)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        }

        // Selected button ko highlight karo
        when (selected) {
            "Monthly" -> btnMonthly.apply {
                setBackgroundResource(R.drawable.btn_selected)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
            }
            "Quarterly" -> btnQuarterly.apply {
                setBackgroundResource(R.drawable.btn_selected)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
            }
            "Yearly" -> btnYearly.apply {
                setBackgroundResource(R.drawable.btn_selected)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
            }
        }
    }

    // Data update karte hain
    private fun updateData(currentAmount: String, totalEarned: String, progress: Int) {
        tvCurrentAmount.text = currentAmount
        tvTotalEarned.text = totalEarned
        progressCircle.progress = progress
    }
}