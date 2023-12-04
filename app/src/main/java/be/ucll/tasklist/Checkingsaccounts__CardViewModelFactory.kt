package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Checkingsaccounts__FragmentCardsViewModel
import be.ucll.tasklist.TaskDao

class Checkingsaccounts__CardViewModelFactory(private val dao: TaskDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Checkingsaccounts__FragmentCardsViewModel::class.java)) {
            return Checkingsaccounts__FragmentCardsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}