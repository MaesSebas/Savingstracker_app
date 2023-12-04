package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter

class TaskItemAdapter() : ListAdapter<Task, TaskItemAdapter.ViewHolder>(TaskDiffItemCallback()) {

     /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val checkBox: CheckBox

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textView)
            checkBox = view.findViewById(R.id.checkBox)
        }

        fun bind(item:Task) {
            textView.text = item.taskName
            checkBox.isChecked = item.taskDone
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       viewHolder.bind(getItem(position))
    }
}