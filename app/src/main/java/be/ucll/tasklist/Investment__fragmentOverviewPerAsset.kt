package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.ucll.tasklist.databinding.InvestmentFragmentOverviewPerAssetBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Investment__CardViewModelFactory
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

        binding.investmentsAssetsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerviewWithData.layoutManager = LinearLayoutManager(context)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.cardChartProgression.setModel(chartEntryModel)
        }

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Investment__RecyclerViewAdapter(newDataList)
            binding.recyclerviewWithData.adapter = recyclerViewAdapter
        }

        binding.button2.setOnClickListener {
            /*
            val dataToPass = "obligations"
            val action = Investment__fragmentOverviewPerAssetDirections
                .actionInvestmentFragmentOverviewPerAssetToInvestmentFragmentInsertTransaction()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
            */


            val dataToPass = Database__TestParcable("test", "test")

            val action = Investment__fragmentOverviewPerAssetDirections
                .actionInvestmentFragmentOverviewPerAssetToInvestmentFragmentInsertTransaction()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }

        return view
    }

    /*
    companion object {
        fun newInstance() = Investment__fragmentOverviewPerAsset()
    }

    private lateinit var viewModel: Investment__FragmentOverviewPerAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.investment__fragment_overview_per_asset, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Investment__FragmentOverviewPerAssetViewModel::class.java)
        // TODO: Use the ViewModel
    }
     */

}