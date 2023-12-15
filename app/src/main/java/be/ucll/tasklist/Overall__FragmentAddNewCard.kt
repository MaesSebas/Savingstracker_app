package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import be.ucll.tasklist.databinding.OverallFragmentAddNewCardBinding
import be.ucll.tasklist.databinding.OverallFragmentInsertTransactionBinding
import com.patrykandpatrick.vico.core.extension.getFieldValue
import com.sebastiaan.savingstrackerapp.Overall__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Overall__InsertTransactionCardViewModelFactory

class Overall__FragmentAddNewCard : Fragment() {
    private var _binding: OverallFragmentAddNewCardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Overall__FragmentAddNewCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //Arguments
        //val typeAccount = Investment__FragmentInsertTransactionArgs.fromBundle(requireArguments()).selectedData
        val typeAccount = "SavingsAccount"

        //ViewModel
        val viewModelFactory = Overall__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Overall__FragmentAddNewCardViewModel::class.java)


        viewModel.insertionSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val dataToPass = "stocks"
                val action = Overall__FragmentAddNewCardDirections
                    .actionOverallFragmentAddNewCardToSavings()
                action.setSelectedData(dataToPass)
                findNavController().navigate(action)
                viewModel.resetInsertionSuccess()
            }
        })

        //Binding
        _binding = OverallFragmentAddNewCardBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.insertCardViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val accountTypesArray = resources.getStringArray(R.array.account_types)
        val selectedItemIndex = accountTypesArray.indexOf(typeAccount as String)
        binding.spinnerTransactionType.setSelection(selectedItemIndex)

        binding.btnAddTransaction.setOnClickListener {
            viewModel.insertNewAccount(
                binding.spinnerTransactionType.selectedItem.toString(),
                binding.editTextAmount.text.toString(),
                binding.editTextCategory.text.toString(),
                binding.editAccountNumber.text.toString(),
            )
        }

        return view
    }

}