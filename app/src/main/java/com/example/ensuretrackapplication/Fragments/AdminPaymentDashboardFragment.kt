package com.example.ensuretrackapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.Adapters.TransactionAdapter
import com.example.ensuretrackapplication.Models.AvatarColor
import com.example.ensuretrackapplication.Models.PaymentStats
import com.example.ensuretrackapplication.Models.Transaction
import com.example.ensuretrackapplication.Models.TransactionStatus
import com.example.ensuretrackapplication.R
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminPaymentDashboardFragment : Fragment() {

    // Views
    private lateinit var tvTotalRevenue: TextView
    private lateinit var tvProgressPercentage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var chipGroup: ChipGroup
    private lateinit var recyclerViewTransactions: RecyclerView
    private lateinit var fabAddPayment: FloatingActionButton
    private lateinit var tvViewAll: TextView

    // Status Card Views
    private lateinit var tvCollectedAmount: TextView
    private lateinit var tvPendingAmount: TextView
    private lateinit var tvOverdueAmount: TextView

    private lateinit var transactionAdapter: TransactionAdapter

    private var paymentStats = PaymentStats(
        totalRevenue = 12450.0,
        targetRevenue = 16000.0,
        collectedAmount = 8200.0,
        collectedCount = 15,
        pendingAmount = 3100.0,
        pendingCount = 5,
        overdueAmount = 1150.0,
        overdueCount = 3
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout first
        val view = inflater.inflate(R.layout.fragment_admin_payment_dashboard, container, false)

        // Initialize views
        initViews(view)

        // Setup components
        setupChipGroup()
        setupRecyclerView()
        setupFAB()

        // Update UI with data
        updateUI()

        return view
    }

    private fun initViews(view: View) {
        // Main revenue card views
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue)
        tvProgressPercentage = view.findViewById(R.id.tvProgressPercentage)
        progressBar = view.findViewById(R.id.progressBar)

        // Filter chips
        chipGroup = view.findViewById(R.id.chipGroup)

        // Status cards
        tvCollectedAmount = view.findViewById(R.id.tvCollectedAmount)
        tvPendingAmount = view.findViewById(R.id.tvPendingAmount)
        tvOverdueAmount = view.findViewById(R.id.tvOverdueAmount)

        // Recent activity
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions)
        tvViewAll = view.findViewById(R.id.tvViewAll)

        // FAB
        fabAddPayment = view.findViewById(R.id.fabAddPayment)
    }

    private fun setupChipGroup() {
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipThisMonth -> {
                    Toast.makeText(requireContext(), "Showing This Month", Toast.LENGTH_SHORT).show()
                    // TODO: Load this month's data
                    loadMonthData("current")
                }
                R.id.chipLastMonth -> {
                    Toast.makeText(requireContext(), "Showing Last Month", Toast.LENGTH_SHORT).show()
                    // TODO: Load last month's data
                    loadMonthData("last")
                }
                R.id.chipThisYear -> {
                    Toast.makeText(requireContext(), "Showing This Year", Toast.LENGTH_SHORT).show()
                    // TODO: Load this year's data
                    loadYearData()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val transactions = getSampleTransactions()

        transactionAdapter = TransactionAdapter(transactions) { transaction ->
            onTransactionClicked(transaction)
        }

        recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
            setHasFixedSize(true)
        }

        tvViewAll.setOnClickListener {
            Toast.makeText(requireContext(), "View All Transactions", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to all transactions screen
        }
    }

    private fun setupFAB() {
        fabAddPayment.setOnClickListener {
            Toast.makeText(requireContext(), "Record New Payment", Toast.LENGTH_SHORT).show()
            // TODO: Open dialog or navigate to add payment screen
            openAddPaymentDialog()
        }
    }

    private fun updateUI() {
        // Update total revenue
        tvTotalRevenue.text = "$${String.format("%,.2f", paymentStats.totalRevenue)}"

        // Update progress bar
        tvProgressPercentage.text = "${paymentStats.progressPercentage}%"
        progressBar.progress = paymentStats.progressPercentage

        // Update status cards
        tvCollectedAmount.text = "$${String.format("%,.0f", paymentStats.collectedAmount)}"
        tvPendingAmount.text = "$${String.format("%,.0f", paymentStats.pendingAmount)}"
        tvOverdueAmount.text = "$${String.format("%,.0f", paymentStats.overdueAmount)}"
    }

    private fun getSampleTransactions(): List<Transaction> {
        return listOf(
            Transaction(
                id = "1",
                clientName = "John Doe",
                policyType = "Life Policy",
                amount = 150.0,
                date = "Today, 10:23 AM",
                status = TransactionStatus.SUCCESS,
                avatarColor = AvatarColor.BLUE
            ),
            Transaction(
                id = "2",
                clientName = "Jane Smith",
                policyType = "Auto",
                amount = 230.0,
                date = "Due Tomorrow",
                status = TransactionStatus.PENDING,
                avatarColor = AvatarColor.PURPLE
            ),
            Transaction(
                id = "3",
                clientName = "Mike Waters",
                policyType = "Home",
                amount = 540.0,
                date = "Due Sept 12",
                status = TransactionStatus.OVERDUE,
                avatarColor = AvatarColor.RED
            ),
            Transaction(
                id = "4",
                clientName = "Sarah Johnson",
                policyType = "Life Policy",
                amount = 320.0,
                date = "Yesterday, 3:15 PM",
                status = TransactionStatus.SUCCESS,
                avatarColor = AvatarColor.BLUE
            ),
            Transaction(
                id = "5",
                clientName = "Robert Brown",
                policyType = "Health",
                amount = 450.0,
                date = "Due Jan 15",
                status = TransactionStatus.PENDING,
                avatarColor = AvatarColor.PURPLE
            )
        )
    }

    private fun onTransactionClicked(transaction: Transaction) {
        Toast.makeText(
            requireContext(),
            "Clicked: ${transaction.clientName} - ${transaction.policyType}",
            Toast.LENGTH_SHORT
        ).show()
        // TODO: Navigate to transaction detail screen
    }

    private fun openAddPaymentDialog() {
        // TODO: Implement add payment dialog
        // Example:
        // val dialog = AddPaymentDialogFragment()
        // dialog.show(childFragmentManager, "AddPaymentDialog")
    }

    private fun loadMonthData(month: String) {
        // TODO: Load data from database or API
        // Update paymentStats and call updateUI()
    }

    private fun loadYearData() {
        // TODO: Load yearly data from database or API
        // Update paymentStats and call updateUI()
    }

    // Optional: Refresh data method
    fun refreshData() {
        // Reload current filter data
        val checkedChipId = chipGroup.checkedChipId
        when (checkedChipId) {
            R.id.chipThisMonth -> loadMonthData("current")
            R.id.chipLastMonth -> loadMonthData("last")
            R.id.chipThisYear -> loadYearData()
        }
    }
}