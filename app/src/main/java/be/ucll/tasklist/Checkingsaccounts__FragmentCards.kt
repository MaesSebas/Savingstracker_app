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
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.CheckingsaccountsFragmentCardsBinding
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sebastiaan.savingstrackerapp.Checkingsaccounts__CardViewModelFactory

class Checkingsaccounts__FragmentCards : Fragment() {
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

        //argmuments
        //val dataTest = Investment__FragmentInsertTransactionArgs.fromBundle(requireArguments()).selectedData

        //ViewModel
        val viewModelFactory = Checkingsaccounts__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Checkingsaccounts__FragmentCardsViewModel::class.java)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.cardChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsAccountLiveData.observe(viewLifecycleOwner) { newDataList ->
            // Update the ViewPager adapter when the data changes
            checkingsaccountsViewPagerAdapter = Checkingsaccounts__ViewPagerAdapter(requireContext(), newDataList)
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

        //move up popup to top screen functionality
        move_up_popup_layout = binding.moveUpPopup
        toggle_move_up_popup_button = binding.toggleMoveUpPopup

        toggle_move_up_popup_button.setOnClickListener {
            toggleHeightMoveUpPopup()
        }

        return view

    }

    private fun toggleHeightMoveUpPopup() {
        val screenHeight = resources.displayMetrics.heightPixels
        val startHeight = move_up_popup_layout.height
        val endHeight = if (isExpanded) {
            (screenHeight * 0.49).toInt() // Change the default height
        } else {
            (screenHeight * 0.92).toInt() // Change to toggled height
        }

        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 500 // Animation duration in milliseconds

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            move_up_popup_layout.layoutParams.height = animatedValue
            move_up_popup_layout.requestLayout()
        }

        animator.start()
        isExpanded = !isExpanded
    }

}