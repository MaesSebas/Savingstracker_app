package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Checkingsaccounts__RecyclerViewAdapter(private val recyclerViewData: List<Database__Transaction>) : RecyclerView.Adapter<Checkingsaccounts__RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.checkingsaccounts__transaction_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = recyclerViewData[position]
        holder.titleTextView.text = transaction.companyName
        holder.descriptionTextView.text = transaction.description
        holder.priceTextView.text = "€" + transaction.amount.toString()
        holder.dateTextView.text = transaction.transactionDate.toString()
    }

    override fun getItemCount(): Int {
        return recyclerViewData.size // Assuming only one item in the RecyclerView
    }
}