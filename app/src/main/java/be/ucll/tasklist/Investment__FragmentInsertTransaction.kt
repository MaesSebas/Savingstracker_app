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
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory

class Investment__FragmentInsertTransaction : Fragment() {

    private var _binding: CheckingsaccountsFragmentCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Checkingsaccounts__FragmentCardsViewModel

    // popup window variables
    private lateinit var move_up_popup_layout: FrameLayout
    private lateinit var toggle_move_up_popup_button: ImageButton
    private var isExpanded = false

    // viewPager
    private lateinit var viewPager: ViewPager
    private lateinit var checkingsaccountsViewPagerAdapter: Checkingsaccounts__ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        //Database
        val dao = Database__TaskDatabase.getInstance(application).databaseTaskDao

        //ViewModel
        val viewModelFactory = Checkingsaccounts__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(Checkingsaccounts__FragmentCardsViewModel::class.java)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.cardChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsAccountLiveData.observe(viewLifecycleOwner) { newDataList ->
            // Update the ViewPager adapter when the data changes
            checkingsaccountsViewPagerAdapter =
                Checkingsaccounts__ViewPagerAdapter(requireContext(), newDataList)
            viewPager.adapter = checkingsaccountsViewPagerAdapter
        }

        //Binding
        _binding = CheckingsaccountsFragmentCardsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.cardsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.totalCardAmount.observe(viewLifecycleOwner) { newTotalCardAmount ->
            val balanceTextView = binding.totalBalanceCards
            balanceTextView.text = "€" + viewModel.totalCardAmount.value.toString()
        }

        //viewPager
        viewPager = binding.creditcardViewPager

        return view
    }


        /*
        companion object {
            fun newInstance() = Investment__FragmentInsertTransaction()
        }

        private lateinit var viewModel: Investment__FragmentInsertTransactionViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.investment__fragment_insert_transaction, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            viewModel =
                ViewModelProvider(this).get(Investment__FragmentInsertTransactionViewModel::class.java)
            // TODO: Use the ViewModel
        }
         */
}