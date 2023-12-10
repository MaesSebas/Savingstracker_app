package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import be.ucll.tasklist.databinding.SavingsaccountFragmentSavingsDetailsBinding
import be.ucll.tasklist.databinding.SavingsaccountsFragmentSavingsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Savings__DetailsCardViewModelFactory
import com.sebastiaan.savingstrackerapp.Savingsaccounts__CardViewModelFactory

class Savingsaccount__FragmentSavingsDetails : Fragment() {

    private var _binding: SavingsaccountFragmentSavingsDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Savingsaccount__FragmentSavingsDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavingsaccountFragmentSavingsDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        val viewModelFactory = Savings__DetailsCardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Savingsaccount__FragmentSavingsDetailsViewModel::class.java)

        binding.savingsDetailsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.savingsAccountsRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.savingsChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsTransactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Savingsaccounts__DetailsRecyclerViewAdapter(newDataList)
            binding.savingsAccountsRecyclerview.adapter = recyclerViewAdapter
        }

        /*
        binding.button2.setOnClickListener {
            val dataToPass = "obligations"
            val action = Savingsaccounts__FragmentSavingsDirections
                .actionSavingsToSavingsaccountFragmentSavingsDetails2()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }
        */

        return view
    }
}