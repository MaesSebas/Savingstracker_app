package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Investment__FragmentInvestment : Fragment() {

    companion object {
        fun newInstance() = Investment__FragmentInvestment()
    }

    private lateinit var viewModel: Investment__FragmentInvestmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.investment__fragment_investment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Investment__FragmentInvestmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}