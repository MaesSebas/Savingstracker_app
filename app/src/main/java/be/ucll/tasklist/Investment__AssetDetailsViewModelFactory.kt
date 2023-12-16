package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Investment__FragmentAssetDetailsViewModel

class Investment__AssetDetailsViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Investment__FragmentAssetDetailsViewModel::class.java)) {
            return Investment__FragmentAssetDetailsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}