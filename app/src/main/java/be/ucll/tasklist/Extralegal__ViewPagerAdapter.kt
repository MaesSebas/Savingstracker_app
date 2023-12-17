package be.ucll.tasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import java.lang.Math.round
import java.util.Objects

class Extralegal__ViewPagerAdapter(private val context: Context, private var dataList: List<Database__AccountsAndTransactions>) : PagerAdapter() {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var itemView: View = inflater.inflate(R.layout.extralegal__meal_voucher_item, container, false)
        if(dataList[position].account.accountName == "Monizze") {
            itemView = inflater.inflate(R.layout.extralegal__eco_voucher_item, container, false)
        }
        if(dataList[position].account.accountName == "AddCardFAKE") {
            val itemView: View = inflater.inflate(R.layout.overall__add_new_card_item, container, false)

            val includedLayout: View = itemView.findViewById(R.id.include)
            includedLayout.setOnClickListener {
                val dataToPassTest =  "ExtraLegalAccount"
                //altijd android name gebruiken van navigation.xml + Directions
                val action = Extralegal__FragmentChequesDirections
                    .actionExtraLegalToOverallFragmentAddNewCard2(dataToPassTest)
                Navigation.findNavController(itemView).navigate(action)
            }

            Objects.requireNonNull(container).addView(itemView)
            return itemView
        }

        //val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.transactions_title)
        val balanceTextView: TextView = itemView.findViewById(R.id.cardBalance)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerviewWithData)
        val navigationInsertButton : Button = itemView.findViewById(R.id.button)


        navigationInsertButton.setOnClickListener {
            val dataToPass = dataList[position].account.accountID
            val action = Extralegal__FragmentChequesDirections
                .actionExtraLegalToOverallFragmentInsertTransaction2(dataToPass)
            Navigation.findNavController(itemView).navigate(action)
        }

        val customData = dataList[position]
        descriptionTextView.text = "Transaction"
        balanceTextView.text = "€" + customData.account.totalBalance


        if (customData.transactions.isEmpty()) {
            itemView.findViewById<TextView>(R.id.noTransactionsMessage).text = "No transactions yet"
            itemView.findViewById<RecyclerView>(R.id.recyclerviewWithData).visibility = View.GONE
        } else {
            itemView.findViewById<TextView>(R.id.noTransactionsMessage).visibility = View.GONE
            val recyclerViewAdapter = Extralegal__RecyclerViewAdapter(customData.transactions)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerViewAdapter
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}