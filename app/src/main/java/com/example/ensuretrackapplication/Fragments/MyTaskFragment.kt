package com.example.ensuretrackapplication.Fragments

import TaskAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.AddNewClientActivity
import com.example.ensuretrackapplication.AddNewTaskActivity
import com.example.ensuretrackapplication.R
import com.example.ensuretrackapplication.databinding.FragmentMyTaskBinding
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlin.jvm.java

class MyTaskFragment : Fragment() {
    lateinit var binding: FragmentMyTaskBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var chipGroup: ChipGroup
    private lateinit var tabLayout: TabLayout
    private lateinit var fabAddTask: ExtendedFloatingActionButton

    private var allTasks = mutableListOf<Task>()
    private var filteredTasks = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // PEHLE VIEW INFLATE KARO
        binding = FragmentMyTaskBinding.inflate(inflater,container,false)



        // PHIR VIEWS INITIALIZE KARO
        initViews(binding.root)
        setupData()
        setupRecyclerView()
        setupListeners()
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(requireContext(), AddNewTaskActivity::class.java)
            startActivity(intent)
        }
        return binding.root


    }

    private fun initViews(view: View) {
        // Fragment mein findViewById nahi chalega, view.findViewById use karo
        recyclerView = view.findViewById(R.id.recyclerViewTasks)
        chipGroup = view.findViewById(R.id.chipGroup)
        tabLayout = view.findViewById(R.id.tabLayout)
        fabAddTask = view.findViewById(R.id.fabAddTask)
    }

    private fun setupData() {
        allTasks = mutableListOf(
            Task(
                id = 1,
                title = "Call John Doe re: Lapsed Policy",
                subtitle = "Policy #88392 • Auto Insurance",
                time = "Due 10:00 AM",
                priority = TaskPriority.URGENT,
                badge = "Policy Lapsed",
                icon = "call",
                isCompleted = false
            ),
            Task(
                id = 2,
                title = "Collect ID Proof for Sarah M.",
                subtitle = "Life Insurance Application",
                time = "Due 11:30 AM",
                priority = TaskPriority.URGENT,
                badge = "Missing Docs",
                icon = "mail",
                isCompleted = false
            ),
            Task(
                id = 3,
                title = "Submit Claim for Jane Smith",
                subtitle = "Claim #99210 • Home Insurance",
                time = "Today, 2:00 PM",
                priority = TaskPriority.HIGH,
                badge = null,
                icon = null,
                isCompleted = false
            ),
            Task(
                id = 4,
                title = "Weekly Team Meeting",
                subtitle = "4:00 PM • Zoom",
                time = null,
                priority = TaskPriority.MEDIUM,
                badge = null,
                icon = "videocam",
                isCompleted = false
            ),
            Task(
                id = 5,
                title = "Review Q4 Sales Goals",
                subtitle = "Anytime today",
                time = null,
                priority = TaskPriority.MEDIUM,
                badge = null,
                icon = "trending_up",
                isCompleted = false
            )
        )
        filteredTasks.addAll(allTasks)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(filteredTasks) { task ->
            task.isCompleted = !task.isCompleted
            taskAdapter.notifyDataSetChanged()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()) // this@MainActivity ki jagah requireContext()
            adapter = taskAdapter
        }
    }

    private fun setupListeners() {
        // Tab listener for Today/Upcoming
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Filter logic for today/upcoming
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Chip filter listener
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipAll -> filterTasks(null)
                R.id.chipPending -> filterTasks(TaskStatus.PENDING)
                R.id.chipOverdue -> filterTasks(TaskStatus.OVERDUE)
            }
        }

        // FAB click
        fabAddTask.setOnClickListener {
            // Add new task logic
        }
    }

    private fun filterTasks(status: TaskStatus?) {
        filteredTasks.clear()

        when (status) {
            null -> filteredTasks.addAll(allTasks)
            TaskStatus.PENDING -> filteredTasks.addAll(allTasks.filter { !it.isCompleted })
            TaskStatus.COMPLETED -> filteredTasks.addAll(allTasks.filter { it.isCompleted })
            TaskStatus.OVERDUE -> filteredTasks.addAll(allTasks.filter {
                it.priority == TaskPriority.URGENT && !it.isCompleted
            })
        }

        taskAdapter.notifyDataSetChanged()
    }
}

// Data Classes
data class Task(
    val id: Int,
    val title: String,
    val subtitle: String,
    val time: String?,
    val priority: TaskPriority,
    val badge: String?,
    val icon: String?,
    var isCompleted: Boolean = false
)

enum class TaskPriority {
    URGENT, HIGH, MEDIUM, LOW
}

enum class TaskStatus {
    PENDING, COMPLETED, OVERDUE
}