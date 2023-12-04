package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Extralegal__FragmentCheques : Fragment() {

    companion object {
        fun newInstance() = Extralegal__FragmentCheques()
    }

    private lateinit var viewModel: Extralegal__FragmentChequesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.extralegal__fragment_cheques, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Extralegal__FragmentChequesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}