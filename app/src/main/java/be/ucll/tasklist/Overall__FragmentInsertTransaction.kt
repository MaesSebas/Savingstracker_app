package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Overall__FragmentInsertTransaction : Fragment() {

    companion object {
        fun newInstance() = Overall__FragmentInsertTransaction()
    }

    private lateinit var viewModel: Overall__FragmentInsertTransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overall___fragment_insert_transaction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Overall__FragmentInsertTransactionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}