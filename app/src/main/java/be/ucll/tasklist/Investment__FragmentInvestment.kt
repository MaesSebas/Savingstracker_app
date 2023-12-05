package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import be.ucll.tasklist.databinding.InvestmentFragmentInvestmentBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Investment__CardViewModelFactory

// fix error after crash io expeption -> ./gradlew clean assembleDebug

class Investment__FragmentInvestment : Fragment() {

    private var _binding: InvestmentFragmentInvestmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: Investment__FragmentInvestmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        //Database
        val dao = TaskDatabase.getInstance(application).taskDao

        _binding = InvestmentFragmentInvestmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = Investment__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Investment__FragmentInvestmentViewModel::class.java)

        binding.investmentViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.cardChartProgression.setModel(chartEntryModel)
        }

        /*
        binding.GoToStocksButton.setOnClickListener {
            val dataToPass = "stocks"
            val action = Investment__InvestmentFragmentDirections
                .actionInvestmentInvestmentFragmentToInvestmentOverviewPerAsset()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }

        binding.GoToEtfsButton.setOnClickListener {
            val dataToPass = "etfs"
            val action = Investment__InvestmentFragmentDirections
                .actionInvestmentInvestmentFragmentToInvestmentOverviewPerAsset()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }

        binding.GoToCryptoButton.setOnClickListener {
            val dataToPass = "crypto"
            val action = Investment__InvestmentFragmentDirections
                .actionInvestmentInvestmentFragmentToInvestmentOverviewPerAsset()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }

        binding.GoToObligationsButton.setOnClickListener {
            val dataToPass = "obligations"
            val action = Investment__InvestmentFragmentDirections
                .actionInvestmentInvestmentFragmentToInvestmentOverviewPerAsset()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }
         */

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Investment__FragmentInvestmentViewModel::class.java)
        // TODO: Use the ViewModel
    }
}