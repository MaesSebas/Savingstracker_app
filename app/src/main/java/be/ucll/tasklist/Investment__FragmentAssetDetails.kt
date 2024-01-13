package be.ucll.tasklist

import android.animation.ValueAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.CheckingsaccountsFragmentCardsBinding
import be.ucll.tasklist.databinding.InvestmentFragmentAssetDetailsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.chart.ChartView
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Investment__AssetDetailsViewModelFactory
import java.lang.Math.round

class Investment__FragmentAssetDetails : Fragment() {

    private var _binding: InvestmentFragmentAssetDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Investment__FragmentAssetDetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        // Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        // ViewModel
        val viewModelFactory = Investment__AssetDetailsViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(Investment__FragmentAssetDetailsViewModel::class.java)

        // Arguments
        val assetAndTransactions =
            Investment__FragmentAssetDetailsArgs.fromBundle(requireArguments()).selectedData
        viewModel.setTransactionsDataAndFungateAsInit(assetAndTransactions)

        // Binding
        _binding = InvestmentFragmentAssetDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.cardsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.button.setOnClickListener {
            val dataToPass = assetAndTransactions.asset.investmentId.toString()
            val action = Investment__FragmentAssetDetailsDirections
                .actionInvestmentFragmentAssetDetailsToInvestmentFragmentAssetTransaction(dataToPass)
            findNavController().navigate(action)
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { newDataList ->

            if (newDataList.transactions.isEmpty()) {
                binding.noTransactionsMessage.text = "No transactions yet"
                binding.recyclerviewTransactionData.visibility = View.GONE
            } else {
                binding.noTransactionsMessage.visibility = View.GONE
                val recyclerViewAdapter =
                    Investment__AssetDetailRecyclerViewAdapter(newDataList.transactions)
                val yourElement = binding.recyclerviewTransactionData
                yourElement.layoutManager = LinearLayoutManager(context)
                yourElement.adapter = recyclerViewAdapter
            }
        }

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            val yourElement = binding.cardChartProgression
            yourElement.setModel(chartEntryModel)
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { graphDataList ->
            binding.nameAsset.text = graphDataList.asset.name
            binding.totalBalanceCards2.text = "€" + round(graphDataList.asset.lastValue).toString()

            if (graphDataList.historicalPriceData.size >= 29) {
                val closePrice0 = graphDataList.historicalPriceData[0].closePrice.replace(",", ".").toDouble()
                val closePrice29 = graphDataList.historicalPriceData[29].closePrice.replace(",", ".").toDouble()
                val percentage =
                    kotlin.math.round((closePrice0 / closePrice29 * 100)).toString() + "%"
                binding.changePercentage.text = percentage
            } else {
                binding.changePercentage.text = "0%"
            }
        }

        return view
    }
}