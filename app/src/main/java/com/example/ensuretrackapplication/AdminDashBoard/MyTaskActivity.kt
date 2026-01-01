//package com.example.ensuretrackapplication.AdminDashBoard
//
//import TaskAdapter
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.cardview.widget.CardView
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ensuretrackapplication.R
//import com.example.ensuretrackapplication.Task
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//
//class MyTaskActivity : AppCompatActivity() {
//
//    private lateinit var tasksRecyclerView: RecyclerView
//    private lateinit var taskAdapter: TaskAdapter
//    private lateinit var todoTab: CardView
//    private lateinit var completedTab: CardView
//    private lateinit var fab: FloatingActionButton
//    private var showCompleted = false
//
//    private val allTasks = mutableListOf(
//        Task(
//            title = "Finalize renewal for Mr. Henderson's policy",
//            clientId = "MH-78910",
//            dueDate = "Today, 5:00 PM",
//            colorResId = R.color.red,
//            isCompleted = false
//        ),
//        Task(
//            title = "Follow up with John Doe on policy renewal",
//            clientId = "JD-12345",
//            dueDate = "Tomorrow",
//            colorResId = R.color.orange,
//            isCompleted = false
//        ),
//        Task(
//            title = "Collect premium payment from Jane Smith",
//            clientId = "JS-67890",
//            dueDate = "Dec 28, 2024",
//            colorResId = R.color.cyan,
//            isCompleted = false
//        ),
//        Task(
//            title = "Schedule annual review with The Clarks",
//            clientId = "CL-54321",
//            dueDate = "Next Week",
//            colorResId = R.color.green,
//            isCompleted = false
//        )
//    )
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_task)
//            // Initialize Views
//            tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
//            todoTab = findViewById(R.id.todoTab)
//            completedTab = findViewById(R.id.completedTab)
//            fab = findViewById(R.id.fab)
//
//            // Setup RecyclerView
//            setupRecyclerView()
//
//            // Setup Tab Listeners
//            setupTabs()
//
//            // Setup FAB
//            fab.setOnClickListener {
//                // Add new task functionality
//                addNewTask()
//            }
//
//            // Initial load
//            loadTasks()
//    }
//    private fun setupRecyclerView() {
//        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
//        taskAdapter = TaskAdapter(mutableListOf())
//        tasksRecyclerView.adapter = taskAdapter
//    }
//
//    private fun setupTabs() {
//        todoTab.setOnClickListener {
//            showCompleted = false
//            updateTabUI()
//            loadTasks()
//        }
//
//        completedTab.setOnClickListener {
//            showCompleted = true
//            updateTabUI()
//            loadTasks()
//        }
//    }
//
//    private fun updateTabUI() {
//        if (showCompleted) {
//            // Completed tab active
//            completedTab.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
//            todoTab.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray_light))
//        } else {
//            // Todo tab active
//            todoTab.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
//            completedTab.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray_light))
//        }
//    }
//
//    private fun loadTasks() {
//        val filteredTasks = if (showCompleted) {
//            allTasks.filter { it.isCompleted }
//        } else {
//            allTasks.filter { !it.isCompleted }
//        }
//        taskAdapter.updateTasks(filteredTasks)
//    }
//
//    private fun addNewTask() {
//        // Add new task logic
//        val newTask = Task(
//            title = "New Task",
//            clientId = "NEW-001",
//            dueDate = "Tomorrow",
//            colorResId = R.color.blue,
//            isCompleted = false
//        )
//        allTasks.add(0, newTask)
//        loadTasks()
//    }
//}