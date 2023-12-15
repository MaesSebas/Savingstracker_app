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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.ucll.tasklist.databinding.SavingsaccountsFragmentSavingsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Savingsaccounts__CardViewModelFactory

class Savingsaccounts__FragmentSavings : Fragment() {

    private var _binding: SavingsaccountsFragmentSavingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Savingsaccounts__FragmentSavingsViewModel

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

        viewModel.totalSavingsAmount.observe(viewLifecycleOwner) { newTotalCardAmount ->
            val balanceTextView = binding.balanceCard
            balanceTextView.text = "€" + viewModel.totalSavingsAmount.value.toString()
        }

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.savingsChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsAccountLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Savingsaccounts__RecyclerViewAdapter(newDataList)
            binding.savingsAccountsRecyclerview.adapter = recyclerViewAdapter
        }

        binding.button2.setOnClickListener {
            val dataToPass = "test"
            val action = Savingsaccounts__FragmentSavingsDirections
                .actionSavingsToOverallFragmentAddNewCard("SavingsAccount")
            findNavController().navigate(action)
        }

        return view
    }
}