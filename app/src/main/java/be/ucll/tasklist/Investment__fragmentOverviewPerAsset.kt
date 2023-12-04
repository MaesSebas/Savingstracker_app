package be.ucll.tasklist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Investment__fragmentOverviewPerAsset : Fragment() {

    companion object {
        fun newInstance() = Investment__fragmentOverviewPerAsset()
    }

    private lateinit var viewModel: Investment__FragmentOverviewPerAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.investment__fragment_overview_per_asset, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(Investment__FragmentOverviewPerAssetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}