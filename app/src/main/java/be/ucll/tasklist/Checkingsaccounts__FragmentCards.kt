package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Checkingsaccounts__FragmentCards : Fragment() {

    companion object {
        fun newInstance() = Checkingsaccounts__FragmentCards()
    }

    private lateinit var viewModel: Checkingsaccounts__FragmentCardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.checkingsaccounts__fragment_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Checkingsaccounts__FragmentCardsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}