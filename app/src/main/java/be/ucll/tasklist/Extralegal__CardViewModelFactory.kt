package com.sebastiaan.savingstrackerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ucll.tasklist.Extralegal__FragmentChequesViewModel
import be.ucll.tasklist.TaskDao

class Extralegal__CardViewModelFactory(private val dao: TaskDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Extralegal__FragmentChequesViewModel::class.java)) {
            return Extralegal__FragmentChequesViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}