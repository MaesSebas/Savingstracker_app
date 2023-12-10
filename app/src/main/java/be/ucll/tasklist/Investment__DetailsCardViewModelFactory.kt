package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Investment__FragmentInvestmentViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Investment__FragmentOverviewPerAssetViewModel

class Investment__DetailsCardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Investment__FragmentOverviewPerAssetViewModel::class.java)) {
            return Investment__FragmentOverviewPerAssetViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}