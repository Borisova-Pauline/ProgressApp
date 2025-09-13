package com.tomli.progressapp.databases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.tomli.progressapp.Applic
import kotlinx.coroutines.launch

class ProgressViewModel(val database: ProgressDB)  :ViewModel() {
    val themes=database.dao.allThemes()

    fun addNewTheme(name: String, color: String) = viewModelScope.launch {
        database.dao.addTheme(name, color)
    }



    companion object{
        val factory: ViewModelProvider.Factory= object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as Applic).database
                return ProgressViewModel(database) as T
            }
        }
    }
}