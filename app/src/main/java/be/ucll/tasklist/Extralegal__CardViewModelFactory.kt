package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Extralegal__FragmentChequesViewModel
import be.ucll.tasklist.Database__TaskDao

class Extralegal__CardViewModelFactory(private val dao: Database__TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Extralegal__FragmentChequesViewModel::class.java)) {
            return Extralegal__FragmentChequesViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}