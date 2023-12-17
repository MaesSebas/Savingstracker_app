package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.round

class Extralegal__RecyclerViewAdapter(private val recyclerViewData: List<Database__Transaction>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_NORMAL = 0
    private val VIEW_TYPE_DIVIDER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DIVIDER) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.overall__divider_item, parent, false)
            DividerViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.checkingsaccounts__transaction_item, parent, false)
            ViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val transaction = recyclerViewData[position]
            holder.titleTextView.text = transaction.companyName
            holder.descriptionTextView.text = transaction.description
            holder.priceTextView.text = "€" + round(transaction.amount).toString()
            holder.dateTextView.text = transaction.transactionDate.toString()
        } else if (holder is DividerViewHolder) {
            val transaction = recyclerViewData[position]
            holder.titleTextView.text = transaction.transactionDate
        }
    }

    override fun getItemCount(): Int {
        return recyclerViewData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (recyclerViewData[position].companyName == "Divider") {
            VIEW_TYPE_DIVIDER
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }
}