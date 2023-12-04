package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Savingsaccount__FragmentSavingsDetails : Fragment() {

    companion object {
        fun newInstance() = Savingsaccount__FragmentSavingsDetails()
    }

    private lateinit var viewModel: Savingsaccount__FragmentSavingsDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.savingsaccount___fragment_savings_details,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Savingsaccount__FragmentSavingsDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}