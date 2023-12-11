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
import be.ucll.tasklist.databinding.InvestmentFragmentAssetDetailsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory
import com.sebastiaan.savingstrackerapp.Investment__AssetDetailsViewModelFactory

class Investment__FragmentAssetDetails : Fragment() {

    private var _binding: InvestmentFragmentAssetDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Investment__FragmentAssetDetailsViewModel

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
        val viewModelFactory = Investment__AssetDetailsViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(Investment__FragmentAssetDetailsViewModel::class.java)

        /*
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
         */

        //Binding
        _binding = InvestmentFragmentAssetDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.cardsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /*
        viewModel.totalCardAmount.observe(viewLifecycleOwner) { newTotalCardAmount ->
            val balanceTextView = binding.totalBalanceCards
            balanceTextView.text = "€" + viewModel.totalCardAmount.value.toString()
        }
         */

        //viewPager
        //viewPager = binding.creditcardViewPager


        return view

    }
}