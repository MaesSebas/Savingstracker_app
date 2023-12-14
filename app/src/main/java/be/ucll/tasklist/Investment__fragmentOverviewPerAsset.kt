package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import be.ucll.tasklist.databinding.InvestmentFragmentOverviewPerAssetBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Investment__DetailsCardViewModelFactory

class Investment__fragmentOverviewPerAsset : Fragment() {

    private var _binding: InvestmentFragmentOverviewPerAssetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Investment__FragmentOverviewPerAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InvestmentFragmentOverviewPerAssetBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        val viewModelFactory = Investment__DetailsCardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Investment__FragmentOverviewPerAssetViewModel::class.java)

        //argmuments
        val assetsAndTransactions = Investment__fragmentOverviewPerAssetArgs.fromBundle(requireArguments()).selectedData.data
        viewModel.setTransactionsDataAndFungateAsInit(assetsAndTransactions)

        binding.investmentsAssetsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerviewWithData.layoutManager = GridLayoutManager(context, 2)

        binding.savingsTitle.text = assetsAndTransactions[0].asset.investmentType

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.cardChartProgression.setModel(chartEntryModel)
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Investment__RecyclerViewAdapter(newDataList)
            binding.recyclerviewWithData.adapter = recyclerViewAdapter
        }

        viewModel.totalCardAmount.observe(viewLifecycleOwner) { newTotalCardAmount ->
            val balanceTextView = binding.balanceCard
            balanceTextView.text = "€" + viewModel.totalCardAmount.value.toString()
        }

        binding.button2.setOnClickListener {
            val dataToPass = DatabaseTestParcable("test", "test")
            val action = Investment__fragmentOverviewPerAssetDirections
                .actionInvestmentFragmentOverviewPerAssetToInvestmentFragmentInsertTransaction(dataToPass)
            findNavController().navigate(action)

        }

        return view
    }
}