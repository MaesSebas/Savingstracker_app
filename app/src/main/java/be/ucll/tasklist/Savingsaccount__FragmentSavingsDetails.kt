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
import be.ucll.tasklist.databinding.SavingsaccountFragmentSavingsDetailsBinding
import be.ucll.tasklist.databinding.SavingsaccountsFragmentSavingsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Savings__DetailsCardViewModelFactory
import com.sebastiaan.savingstrackerapp.Savingsaccounts__CardViewModelFactory
import java.lang.Math.round

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

        //argmuments
        val dataTest = Savingsaccount__FragmentSavingsDetailsArgs.fromBundle(requireArguments()).selectedData
        viewModel.setTransactionsDataAndFungateAsInit(dataTest.data)

        binding.savingsDetailsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.savingsAccountsRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.totalAmount.observe(viewLifecycleOwner) { graphDataList ->
            binding.balanceCard.text = "€" + round(graphDataList).toString()
        }

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.savingsChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsTransactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Savingsaccounts__DetailsRecyclerViewAdapter(newDataList)
            binding.savingsAccountsRecyclerview.adapter = recyclerViewAdapter
        }


        binding.button2.setOnClickListener {
            val dataToPass = dataTest.data.account.accountID
            val action = Savingsaccount__FragmentSavingsDetailsDirections
                .actionSavingsaccountFragmentSavingsDetailsToOverallFragmentInsertTransaction(dataToPass)
            findNavController().navigate(action)
        }

        return view
    }
}