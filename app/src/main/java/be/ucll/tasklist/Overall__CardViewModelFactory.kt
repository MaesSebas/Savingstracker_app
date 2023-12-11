package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.Database__TaskDao
import be.ucll.tasklist.Overall__FragmentAddNewCardViewModel

class Overall__CardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Overall__FragmentAddNewCardViewModel::class.java)) {
            return Overall__FragmentAddNewCardViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}