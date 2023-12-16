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

    // viewPager
    private lateinit var viewPager: ViewPager
    private lateinit var checkingsaccountsViewPagerAdapter: Checkingsaccounts__ViewPagerAdapter

    // popup window variables
    private lateinit var move_up_popup_layout: FrameLayout
    private lateinit var toggle_move_up_popup_button: ImageButton
    private var isExpanded = false

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

        //move up popup to top screen functionality
        move_up_popup_layout = binding.moveUpPopup
        toggle_move_up_popup_button = binding.toggleMoveUpPopup

        toggle_move_up_popup_button.setOnClickListener {
            toggleHeightMoveUpPopup()
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter =
                Investment__AssetDetailRecyclerViewAdapter(newDataList.transactions)

            val includedLayout =
                view.findViewById<ConstraintLayout>(R.id.includedGraphInvestmentAssetDetails)
            val yourElement = includedLayout.findViewById<RecyclerView>(R.id.recyclerviewTransactionData)
            yourElement.layoutManager = LinearLayoutManager(context)
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

            val closePrice0 = graphDataList.historicalPriceData[0].closePrice.replace(",", ".").toDouble()
            val closePrice29 = graphDataList.historicalPriceData[29].closePrice.replace(",", ".").toDouble()
            val percentage =
                kotlin.math.round((closePrice0 / closePrice29 * 100)).toString() + "%"
            binding.changePercentage.text = percentage
        }

        binding.changePercentage.text =

        return view
    }

    private fun toggleHeightMoveUpPopup() {
        val screenHeight = resources.displayMetrics.heightPixels
        val startHeight = move_up_popup_layout.height
        val endHeight = if (isExpanded) {
            (screenHeight * 0.70).toInt() // Change the default height
        } else {
            (screenHeight * 0.95).toInt() // Change to toggled height
        }

        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 500 // Animation duration in milliseconds

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            move_up_popup_layout.layoutParams.height = animatedValue
            move_up_popup_layout.requestLayout()
        }

        animator.start()
        isExpanded = !isExpanded
    }
}