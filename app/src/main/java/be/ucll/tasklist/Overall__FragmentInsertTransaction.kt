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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.OverallFragmentInsertTransactionBinding
import com.patrykandpatrick.vico.core.extension.getFieldValue
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Overall__InsertTransactionCardViewModelFactory

class Overall__FragmentInsertTransaction : Fragment() {
    private var _binding: OverallFragmentInsertTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Overall__FragmentInsertTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //ViewModel
        val viewModelFactory = Overall__InsertTransactionCardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Overall__FragmentInsertTransactionViewModel::class.java)

        viewModel.insertionSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val dataToPass = "stocks"
                val action = Overall__FragmentInsertTransactionDirections
                    .actionOverallFragmentInsertTransactionToCards()
                action.setSelectedData(dataToPass)
                findNavController().navigate(action)
                viewModel.resetInsertionSuccess()
            }
        })

        //Binding
        _binding = OverallFragmentInsertTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.insertTransactionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnAddTransaction.setOnClickListener {
            viewModel.insertCardTransaction(
                binding.companyName.text.toString(),
                binding.description.text.toString(),
                binding.transactiondate.text.toString(),
                binding.category.text.toString(),
                binding.amount.text.toString().toDoubleOrNull() ?: 0.0,
                "test")
        }

        //binding.spinnerTransactionType.selectedItem.toString())
        return view
    }
}