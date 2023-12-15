package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class Savingsaccounts__RecyclerViewAdapter(private val recyclerViewData: List<Database__AccountsAndTransactions>) : RecyclerView.Adapter<Savingsaccounts__RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.accountNameTextView)
        val balanceTextView: TextView = itemView.findViewById(R.id.balanceTextView)
        val accountNumberTextView: TextView = itemView.findViewById(R.id.accountNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.savingssaccounts__savingsaccount_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = recyclerViewData[position]
        holder.titleTextView.text = transaction.account.accountName
        holder.accountNumberTextView.text = transaction.account.accountNumber
        holder.balanceTextView.text = "€" + transaction.account.totalBalance

        holder.itemView.setOnClickListener {
            val dataToPassTest =  DatabaseParcableAccountAndTransactions(transaction)
            val action = Savingsaccounts__FragmentSavingsDirections
                .actionSavingsToSavingsaccountFragmentSavingsDetails2(dataToPassTest)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return recyclerViewData.size
    }
}