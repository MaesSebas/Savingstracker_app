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
        val itemView: View = inflater.inflate(R.layout.extralegal__meal_voucher_item, container, false)

        //val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.transactions_title)
        val balanceTextView: TextView = itemView.findViewById(R.id.cardBalance)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerviewWithData)
        val navigationInsertButton : Button = itemView.findViewById(R.id.button)


        navigationInsertButton.setOnClickListener {
            val dataToPass = "obligations"
            val action = Extralegal__FragmentChequesDirections
                .actionExtraLegalToOverallFragmentInsertTransaction2()
            action.setSelectedData(dataToPass)
            Navigation.findNavController(itemView).navigate(action)
        }

        val customData = dataList[position]
        descriptionTextView.text = "Transaction"
        balanceTextView.text = customData.account.totalBalance


        val recyclerViewAdapter = Extralegal__RecyclerViewAdapter(customData.transactions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerViewAdapter

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}