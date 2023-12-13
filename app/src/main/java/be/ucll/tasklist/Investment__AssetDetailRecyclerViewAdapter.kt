package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.round

class Investment__AssetDetailRecyclerViewAdapter(private val recyclerViewData: List<Database__AssetTransaction>) : RecyclerView.Adapter<Investment__AssetDetailRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionData: TextView = itemView.findViewById(R.id.titleTextView)
        val assetQuantity: TextView = itemView.findViewById(R.id.dateTextView)
        val assetValue: TextView = itemView.findViewById(R.id.priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.investment__transaction_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = recyclerViewData[position]
        holder.transactionData.text = transaction.transactionDate
        holder.assetQuantity.text = transaction.quantity.toString()
        holder.assetValue.text = round(transaction.value!!).toString()
    }

    override fun getItemCount(): Int {
        return recyclerViewData.size // Assuming only one item in the RecyclerView
    }
}