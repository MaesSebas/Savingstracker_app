package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import be.ucll.tasklist.databinding.ExtralegalFragmentChequesBinding
import com.sebastiaan.savingstrackerapp.Extralegal__CardViewModelFactory

class Extralegal__FragmentCheques : Fragment() {

    private var _binding: ExtralegalFragmentChequesBinding? = null // Change to FragmentCardsBinding
    private val binding get() = _binding!!

    private lateinit var viewModel: Extralegal__FragmentChequesViewModel // Change to the correct ViewModel class

    // viewPager
    private lateinit var viewPager: ViewPager
    private lateinit var extraLegalViewPagerAdapter: Extralegal__ViewPagerAdapter // You need to create this adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExtralegalFragmentChequesBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = Extralegal__CardViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Extralegal__FragmentChequesViewModel::class.java)

        binding.extralegalViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewPager = binding.extralegalViewPager

        viewModel.extraLegalCardsLiveData.observe(viewLifecycleOwner) { newDataList ->
            extraLegalViewPagerAdapter = Extralegal__ViewPagerAdapter(requireContext(), newDataList)
            viewPager.adapter = extraLegalViewPagerAdapter
        }

        return view
    }
}
