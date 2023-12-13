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

class Savingsaccount__FragmentSavingsDetails : Fragment() {

    private var _binding: SavingsaccountFragmentSavingsDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Savingsaccount__FragmentSavingsDetailsViewModel

    // popup window variables
    private lateinit var move_up_popup_layout: FrameLayout
    private lateinit var toggle_move_up_popup_button: ImageButton
    private var isExpanded = false

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

        binding.savingsDetailsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.savingsAccountsRecyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.graphLiveData.observe(viewLifecycleOwner) { graphDataList ->
            val chartEntryModel = entryModelOf(*graphDataList.map { it.toFloat() }.toTypedArray())
            binding.savingsChartProgression.setModel(chartEntryModel)
        }

        viewModel.checkingsTransactionsLiveData.observe(viewLifecycleOwner) { newDataList ->
            val recyclerViewAdapter = Savingsaccounts__DetailsRecyclerViewAdapter(newDataList)
            binding.savingsAccountsRecyclerview.adapter = recyclerViewAdapter
        }


        binding.button2.setOnClickListener {
            val dataToPass = 1L
            val action = Savingsaccount__FragmentSavingsDetailsDirections
                .actionSavingsaccountFragmentSavingsDetailsToOverallFragmentInsertTransaction(dataToPass)
            findNavController().navigate(action)
        }

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