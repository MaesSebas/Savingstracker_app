package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Investment__FragmentInsertTransaction : Fragment() {

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

}