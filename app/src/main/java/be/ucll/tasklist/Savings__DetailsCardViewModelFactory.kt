package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Savingsaccount__FragmentSavingsDetailsViewModel

class Savings__DetailsCardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Savingsaccount__FragmentSavingsDetailsViewModel::class.java)) {
            return Savingsaccount__FragmentSavingsDetailsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}