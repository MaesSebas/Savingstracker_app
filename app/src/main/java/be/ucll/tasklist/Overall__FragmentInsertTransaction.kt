package be.ucll.tasklist

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.OverallFragmentInsertTransactionBinding
import com.patrykandpatrick.vico.core.extension.getFieldValue
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Overall__InsertTransactionCardViewModelFactory
import java.util.Calendar

class Overall__FragmentInsertTransaction : Fragment() {
    private var _binding: OverallFragmentInsertTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Overall__FragmentInsertTransactionViewModel

    //lateinit var dateEdt: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //Argmuments
        val accountId = Overall__FragmentInsertTransactionArgs.fromBundle(requireArguments()).selectedData

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


        binding.transactiondate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.transactiondate.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnAddTransaction.setOnClickListener {
            val companyName = binding.companyName.text.toString().trim()
            val description = binding.description.text.toString().trim()
            val transactionDate = binding.transactiondate.toString().trim()
            val category = binding.category.text.toString().trim()
            val amountText = binding.amount.text.toString().trim()

            if (companyName.isEmpty() || description.isEmpty() || transactionDate.isEmpty() ||
                category.isEmpty() || amountText.isEmpty()
            ) {
                Toast.makeText(context,
                    HtmlCompat.fromHtml("<font color='white'>Please fill in all the fields</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.insertCardTransaction(
                accountId,
                binding.companyName.text.toString(),
                binding.description.text.toString(),
                binding.transactiondate.toString(),
                binding.category.text.toString(),
                binding.amount.text.toString().toDoubleOrNull() ?: 0.0,
                "test")
        }

        //binding.spinnerTransactionType.selectedItem.toString())
        return view
    }
}