package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.Database__TaskDao

class Checkingsaccounts__CardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Checkingsaccounts__FragmentCardsViewModel::class.java)) {
            return Checkingsaccounts__FragmentCardsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}