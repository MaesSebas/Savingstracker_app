package be.ucll.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.round

class Investment__RecyclerViewAdapter(private val recyclerViewData: List<DatabaseAssetAndTransactions>) : RecyclerView.Adapter<Investment__RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val investmentLogoImageView: TextView = itemView.findViewById(R.id.investmentLogo)
        val investmentNameTextView: TextView = itemView.findViewById(R.id.investmentName)
        val investmentValueTextView: TextView = itemView.findViewById(R.id.investmentValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.investments__investment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = recyclerViewData[position]
        holder.investmentNameTextView.text = transaction.asset.name
        holder.investmentValueTextView.text = "€" + round(transaction.asset.lastValue).toString()

        holder.itemView.setOnClickListener {
            val dataToPassTest =  recyclerViewData[position]
            //altijd android name gebruiken van navigation.xml + Directions
            val action = Investment__fragmentOverviewPerAssetDirections
                .actionInvestmentFragmentOverviewPerAssetToInvestmentFragmentAssetDetails2(dataToPassTest)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return recyclerViewData.size // Assuming only one item in the RecyclerView
    }
}