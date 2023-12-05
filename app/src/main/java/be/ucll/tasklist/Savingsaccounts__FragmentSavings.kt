package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import be.ucll.tasklist.databinding.SavingsaccountsFragmentSavingsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Savingsaccounts__CardViewModelFactory

class Savingsaccounts__FragmentSavings : Fragment() {

    private var _binding: SavingsaccountsFragmentSavingsBinding? = null // Change to FragmentCardsBinding
    private val binding get() = _binding!!

    private lateinit var viewModel: Savingsaccounts__FragmentSavingsViewModel // Change to the correct ViewModel class

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavingsaccountsFragmentSavingsBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        val viewModelFactory = Savingsaccounts__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Savingsaccounts__FragmentSavingsViewModel::class.java)

        binding.savingsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.savingsAccountsRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.savingsChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsAccountLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Savingsaccounts__RecyclerViewAdapter(newDataList)
            binding.savingsAccountsRecyclerview.adapter = recyclerViewAdapter
        }

        /*
        binding.button2.setOnClickListener {
            val dataToPass = "obligations"
            val action = savingsaccounts__savingsDirections
                .actionSavingsToOverallFragmentAddNewCard()
            action.setSelectedData(dataToPass)
            findNavController().navigate(action)
        }
        */

        return view
    }

    /*
    companion object {
        fun newInstance() = Savingsaccounts__FragmentSavings()
    }

    private lateinit var viewModel: Savingsaccounts__FragmentSavingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.savingsaccounts__fragment_savings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Savingsaccounts__FragmentSavingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

     */

}