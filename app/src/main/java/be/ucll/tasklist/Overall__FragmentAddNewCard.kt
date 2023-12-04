package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Overall__FragmentAddNewCard : Fragment() {

    companion object {
        fun newInstance() = Overall__FragmentAddNewCard()
    }

    private lateinit var viewModel: Overall__FragmentAddNewCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overall__fragment_add_new_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Overall__FragmentAddNewCardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}