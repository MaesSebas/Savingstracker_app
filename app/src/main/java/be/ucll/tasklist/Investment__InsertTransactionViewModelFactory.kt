package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Investment__FragmentInsertTransactionViewModel

class Investment__InsertTransactionViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Investment__FragmentInsertTransactionViewModel::class.java)) {
            return Investment__FragmentInsertTransactionViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}