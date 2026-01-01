package com.example.ensuretrackapplication.Fragments
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.AddNewClientActivity
import com.example.ensuretrackapplication.R
import com.example.ensuretrackapplication.databinding.FragmentClient2Binding

class ClientFragment2 : Fragment() {

    private lateinit var binding: FragmentClient2Binding
    private lateinit var adapter: ClientsAdapter
    private val allClients = mutableListOf<Client>()
    private val filteredClients = mutableListOf<Client>()
    private var currentFilter = "All"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClient2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClients()
        setupRecyclerView()
        setupSearch()
        setupFilters()
        setupFab()
    }

    private fun setupClients() {
        allClients.addAll(listOf(
            Client("Sarah Jenkins", "POL-8821", "Oct 24, 2023", "Active", "https://example.com/sarah.jpg"),
            Client("Michael Chen", "POL-9932", "Yesterday", "Overdue", "https://example.com/michael.jpg"),
            Client("Emily Davis", "POL-1102", "Nov 15, 2023", "Pending", "https://example.com/emily.jpg"),
            Client("David Wilson", "POL-4421", "Dec 01, 2023", "Active", "https://example.com/david.jpg"),
            Client("Amanda Johnson", "POL-6639", "Dec 12, 2023", "Active", null)
        ))
        filteredClients.addAll(allClients)
    }

    private fun setupRecyclerView() {
        adapter = ClientsAdapter(filteredClients)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterClients(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilters() {
        binding.chipAll.setOnClickListener {
            currentFilter = "All"
            updateChipSelection()
            filterClients(binding.searchEditText.text.toString())
        }

        binding.chipOverdue.setOnClickListener {
            currentFilter = "Overdue"
            updateChipSelection()
            filterClients(binding.searchEditText.text.toString())
        }

        binding.chipActive.setOnClickListener {
            currentFilter = "Active"
            updateChipSelection()
            filterClients(binding.searchEditText.text.toString())
        }

        binding.chipPending.setOnClickListener {
            currentFilter = "Pending"
            updateChipSelection()
            filterClients(binding.searchEditText.text.toString())
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddNewClientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateChipSelection() {
        binding.chipAll.isChecked = currentFilter == "All"
        binding.chipOverdue.isChecked = currentFilter == "Overdue"
        binding.chipActive.isChecked = currentFilter == "Active"
        binding.chipPending.isChecked = currentFilter == "Pending"
    }

    private fun filterClients(searchText: String) {
        filteredClients.clear()

        for (client in allClients) {
            val matchesSearch = searchText.isEmpty() ||
                    client.name.contains(searchText, ignoreCase = true) ||
                    client.policyNumber.contains(searchText, ignoreCase = true)

            val matchesFilter = currentFilter == "All" || client.status == currentFilter

            if (matchesSearch && matchesFilter) {
                filteredClients.add(client)
            }
        }

        adapter.notifyDataSetChanged()
    }

    // ============================================
    // Data Class - Client Model
    // ============================================
    data class Client(
        val name: String,
        val policyNumber: String,
        val dueDate: String,
        val status: String,
        val imageUrl: String?
    )

    // ============================================
    // RecyclerView Adapter
    // ============================================
    class ClientsAdapter(private val clients: List<Client>) :
        RecyclerView.Adapter<ClientsAdapter.ClientViewHolder>() {

        class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.findViewById(R.id.nameTextView)
            val policyTextView: TextView = view.findViewById(R.id.policyTextView)
            val dueDateTextView: TextView = view.findViewById(R.id.dueDateTextView)
            val statusTextView: TextView = view.findViewById(R.id.statusTextView)
            val avatarImageView: ImageView = view.findViewById(R.id.avatarImageView)
            val avatarTextView: TextView = view.findViewById(R.id.avatarTextView)
            val warningIcon: ImageView = view.findViewById(R.id.warningIcon)
            val redBar: View = view.findViewById(R.id.redBar)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_client, parent, false)
            return ClientViewHolder(view)
        }

        override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
            val client = clients[position]

            holder.nameTextView.text = client.name
            holder.policyTextView.text = "Policy: #${client.policyNumber}"
            holder.dueDateTextView.text = "Due: ${client.dueDate}"

            holder.statusTextView.text = client.status
            when (client.status) {
                "Active" -> {
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_status_active)
                    holder.statusTextView.setTextColor(Color.parseColor("#059669"))
                }
                "Overdue" -> {
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_status_overdue)
                    holder.statusTextView.setTextColor(Color.parseColor("#DC2626"))
                }
                "Pending" -> {
                    holder.statusTextView.setBackgroundResource(R.drawable.bg_status_pending)
                    holder.statusTextView.setTextColor(Color.parseColor("#6B7280"))
                }
            }

            if (client.status == "Overdue") {
                holder.warningIcon.visibility = View.VISIBLE
                holder.redBar.visibility = View.VISIBLE
                holder.dueDateTextView.text = "Due: Yesterday"
                holder.dueDateTextView.setTextColor(Color.parseColor("#DC2626"))
            } else {
                holder.warningIcon.visibility = View.GONE
                holder.redBar.visibility = View.GONE
                holder.dueDateTextView.setTextColor(Color.parseColor("#6B7280"))
            }

            if (client.imageUrl != null) {
                holder.avatarImageView.visibility = View.VISIBLE
                holder.avatarTextView.visibility = View.GONE
            } else {
                holder.avatarImageView.visibility = View.GONE
                holder.avatarTextView.visibility = View.VISIBLE
                val initials = client.name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                holder.avatarTextView.text = initials
            }
        }

        override fun getItemCount() = clients.size
    }
}

