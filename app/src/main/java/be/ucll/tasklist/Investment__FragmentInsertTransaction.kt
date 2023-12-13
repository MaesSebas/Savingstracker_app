package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
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


        //Binding
        _binding = InvestmentFragmentInsertTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.insertInvestmentTransactionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return view
    }
}