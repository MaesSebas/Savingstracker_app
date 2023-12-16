package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Extralegal__FragmentChequesViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Overall__FragmentInsertTransactionViewModel

class Overall__InsertTransactionCardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Overall__FragmentInsertTransactionViewModel::class.java)) {
            return Overall__FragmentInsertTransactionViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}