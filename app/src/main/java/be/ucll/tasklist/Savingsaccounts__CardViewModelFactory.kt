package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Savingsaccounts__FragmentSavingsViewModel
import be.ucll.tasklist.Database__TaskDao

class Savingsaccounts__CardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Savingsaccounts__FragmentSavingsViewModel::class.java)) {
            return Savingsaccounts__FragmentSavingsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}