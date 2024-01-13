package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Extralegal__FragmentChequesViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Investment__FragmentAssetTransactionViewModel
import be.ucll.tasklist.Investment__FragmentInsertTransactionViewModel
import be.ucll.tasklist.Overall__FragmentInsertTransactionViewModel

class Investment__InsertTransactionAssetModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Investment__FragmentAssetTransactionViewModel::class.java)) {
            return Investment__FragmentAssetTransactionViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}