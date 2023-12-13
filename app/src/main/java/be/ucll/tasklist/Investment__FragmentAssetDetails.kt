package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    // viewPager
    private lateinit var viewPager: ViewPager
    private lateinit var checkingsaccountsViewPagerAdapter: Checkingsaccounts__ViewPagerAdapter

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

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter =
                Investment__AssetDetailRecyclerViewAdapter(newDataList.transactions)

            val includedLayout =
                view.findViewById<ConstraintLayout>(R.id.includedGraphInvestmentAssetDetails)
            val yourElement = includedLayout.findViewById<RecyclerView>(R.id.recyclerviewWithData)
            yourElement.adapter = recyclerViewAdapter
        }

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            val includedLayout =
                view.findViewById<ConstraintLayout>(R.id.includedGraphInvestmentAssetDetails)
            val yourElement = includedLayout.findViewById<ChartView>(R.id.card_chart_progression)
            yourElement.setModel(chartEntryModel)
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { graphDataList ->
            binding.nameAsset.text = graphDataList.asset.name
            binding.totalBalanceCards2.text = "€" + round(graphDataList.asset.lastValue).toString()
        }

        return view
    }
}