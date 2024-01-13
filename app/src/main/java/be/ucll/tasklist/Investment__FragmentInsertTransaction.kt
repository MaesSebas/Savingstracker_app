package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.CheckingsaccountsFragmentCardsBinding
import be.ucll.tasklist.databinding.InvestmentFragmentInsertTransactionBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Investment__InsertTransactionViewModelFactory

class Investment__FragmentInsertTransaction : Fragment() {

    private var _binding: InvestmentFragmentInsertTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Investment__FragmentInsertTransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val dataTest = Investment__FragmentInsertTransactionArgs.fromBundle(requireArguments()).selectedData

        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //ViewModel
        val viewModelFactory = Investment__InsertTransactionViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(Investment__FragmentInsertTransactionViewModel::class.java)

        //argmuments
        val dataTest = Investment__FragmentInsertTransactionArgs.fromBundle(requireArguments()).selectedData

        //Binding
        _binding = InvestmentFragmentInsertTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.insertInvestmentTransactionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.insertionSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val navController = findNavController()
                navController.navigateUp()
                viewModel.resetInsertionSuccess()
            }
        })

        binding.btnAddAsset.setOnClickListener {
            val assetType = binding.spinnerTransactionType.selectedItem.toString()
            val assetName = binding.assetName.text.toString().trim()
            val tickerAsset = binding.ticker.text.toString().trim()
            val assetAmount = binding.assetAmount.text.toString().trim()
            val currentValueAsset = binding.currentValue.text.toString().trim()

            if (assetType.isEmpty() || assetName.isEmpty() || tickerAsset.isEmpty() ||
                assetAmount.isEmpty() || currentValueAsset.isEmpty()) {
                Toast.makeText(context,
                    HtmlCompat.fromHtml("<font color='white'>Please fill in all the fields</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.insertAssetTransaction(
                investmentType = assetType,
                nameAsset = assetName,
                tickerAsset = tickerAsset,
                quantityAsset = assetAmount.toInt(),
                valueAsset = currentValueAsset.toDouble(),
                accountId = 7,)
        }

        return view
    }
}