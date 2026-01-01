import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.Fragments.Task
import com.example.ensuretrackapplication.Fragments.TaskPriority
import com.example.ensuretrackapplication.R
import com.google.android.material.chip.Chip

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskChecked: (Task) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_URGENT = 1
        private const val VIEW_TYPE_HIGH = 2
        private const val VIEW_TYPE_MEDIUM = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (tasks[position].priority) {
            TaskPriority.URGENT -> VIEW_TYPE_URGENT
            TaskPriority.HIGH -> VIEW_TYPE_HIGH
            TaskPriority.MEDIUM, TaskPriority.LOW -> VIEW_TYPE_MEDIUM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_URGENT -> {
                val view = inflater.inflate(R.layout.item_task_urgent, parent, false)
                UrgentTaskViewHolder(view)
            }
            VIEW_TYPE_HIGH -> {
                val view = inflater.inflate(R.layout.item_task_high, parent, false)
                HighTaskViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_task_medium, parent, false)
                MediumTaskViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]

        when (holder) {
            is UrgentTaskViewHolder -> holder.bind(task, onTaskChecked)
            is HighTaskViewHolder -> holder.bind(task, onTaskChecked)
            is MediumTaskViewHolder -> holder.bind(task, onTaskChecked)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // ViewHolder for URGENT tasks (Red border)
    inner class UrgentTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chipBadge: Chip = itemView.findViewById(R.id.chipBadge)
        private val taskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val taskSubtitle: TextView = itemView.findViewById(R.id.tvTaskSubtitle)
        private val taskTime: TextView = itemView.findViewById(R.id.tvTaskTime)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.checkboxTask)
        private val btnAction: ImageButton = itemView.findViewById(R.id.btnAction)

        fun bind(task: Task, onChecked: (Task) -> Unit) {
            // Badge display
            if (task.badge != null) {
                chipBadge.visibility = View.VISIBLE
                chipBadge.text = task.badge
            } else {
                chipBadge.visibility = View.GONE
            }

            // Task details
            taskTitle.text = task.title
            taskSubtitle.text = task.subtitle
            taskTime.text = task.time ?: ""

            // Checkbox state
            taskCheckbox.isChecked = task.isCompleted
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onChecked(task)
            }

            // Action button
            btnAction.setOnClickListener {
                // Handle action (call/email)
            }
        }
    }

    // ViewHolder for HIGH priority tasks (Orange border)
    inner class HighTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val taskSubtitle: TextView = itemView.findViewById(R.id.tvTaskSubtitle)
        private val taskTime: TextView = itemView.findViewById(R.id.tvTaskTime)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.checkboxTask)

        fun bind(task: Task, onChecked: (Task) -> Unit) {
            taskTitle.text = task.title
            taskSubtitle.text = task.subtitle
            taskTime.text = task.time ?: ""

            taskCheckbox.isChecked = task.isCompleted
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onChecked(task)
            }
        }
    }

    // ViewHolder for MEDIUM priority tasks (Blue border)
    inner class MediumTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val taskSubtitle: TextView = itemView.findViewById(R.id.tvTaskSubtitle)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.checkboxTask)

        fun bind(task: Task, onChecked: (Task) -> Unit) {
            taskTitle.text = task.title
            taskSubtitle.text = task.subtitle

            taskCheckbox.isChecked = task.isCompleted
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onChecked(task)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}