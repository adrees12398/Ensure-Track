import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ensuretrackapplication.R
import com.example.ensuretrackapplication.Task

class TaskAdapter(private var tasks: MutableList<Task>,): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        var view = inflate.inflate(R.layout.item_task,parent,false);
        return  TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val context = holder.itemView.context

        // Set color border
        holder.colorBorder.setBackgroundColor(
            ContextCompat.getColor(context, task.colorResId)
        )

        // Set task details
        holder.taskTitle.text = task.title
        holder.taskClient.text = "Client: ${task.clientId}"
        holder.taskDueDate.text = "Due: ${task.dueDate}"

        // Set checkbox state
        holder.taskCheckbox.isChecked = task.isCompleted

        // Set due date color
        if (task.dueDate.contains("Today", ignoreCase = true)) {
            holder.taskDueDate.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            holder.taskDueDate.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
        }

        // Checkbox listener
        holder.taskCheckbox.setOnCheckedChangeListener { _, isChecked ->

        }

    }

    override fun getItemCount(): Int {
     return tasks.size
    }

    inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val colorBorder: View = itemView.findViewById(R.id.colorBorder)
        val taskCheckbox: CheckBox = itemView.findViewById(R.id.taskCheckbox)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskClient: TextView = itemView.findViewById(R.id.taskClient)
        val taskDueDate: TextView = itemView.findViewById(R.id.taskDueDate)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}