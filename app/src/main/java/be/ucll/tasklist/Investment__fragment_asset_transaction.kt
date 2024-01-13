package be.ucll.tasklist

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import be.ucll.tasklist.databinding.InvestmentFragmentAssetTransactionBinding
import com.sebastiaan.savingstrackerapp.Investment__InsertTransactionAssetModelFactory
import java.util.Calendar

class Investment__fragment_asset_transaction : Fragment() {
    private var _binding: InvestmentFragmentAssetTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Investment__FragmentAssetTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //Argmuments
        val investmentId = Investment__fragment_asset_transactionArgs.fromBundle(requireArguments()).selectedData

        //ViewModel
        val viewModelFactory = Investment__InsertTransactionAssetModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Investment__FragmentAssetTransactionViewModel::class.java)

        viewModel.insertionSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                /*
                val dataToPass = "stocks"
                val action = Overall__FragmentInsertTransactionDirections
                    .actionOverallFragmentInsertTransactionToCards()
                action.setSelectedData(dataToPass)
                findNavController().navigate(action)
                 */
                val navController = findNavController()
                navController.navigateUp()
                viewModel.resetInsertionSuccess()
            }
        })

        //Binding
        _binding = InvestmentFragmentAssetTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.insertTransactionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.transactiondate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    /*
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.transactiondate.setText(dat)
                     */
                    val formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
                    binding.transactiondate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnAddTransaction.setOnClickListener {
            val expenseOrIncome = binding.spinnerTransactionType.selectedItem.toString()
            val quantity = binding.quantity.text.toString().trim()
            val value = binding.value.text.toString().trim()
            val transactionDate = binding.transactiondate.text.trim().toString()

            if (quantity.isEmpty() || quantity.isEmpty() || value.isEmpty() || transactionDate.isEmpty()
            ) {
                Toast.makeText(context,
                    HtmlCompat.fromHtml("<font color='white'>Please fill in all the fields</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.insertAssetTransaction(
                expenseOrIncome,
                transactionDate,
                investmentId.toLong(),
                quantity.toInt(),
                value.toDouble())
        }



        return view
    }
}