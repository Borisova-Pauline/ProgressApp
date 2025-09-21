package com.tomli.progressapp.databases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tomli.progressapp.Applic
import com.tomli.progressapp.TypeScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProgressViewModel(val database: ProgressDB)  :ViewModel() {
    val themes=database.dao.allThemes()

    var oneTheme = Themes(0, " ", "Black")
    fun getOneTheme(id: Int)= viewModelScope.launch {
        oneTheme = database.dao.oneTheme(id)
    }
    fun addNewTheme(name: String, color: String) = viewModelScope.launch {
        database.dao.addTheme(name, color)
    }
    fun changeTheme(id: Int, name: String, color: String) = viewModelScope.launch {
        database.dao.changeTheme(id, name, color)
    }
    fun deleteTheme(id: Int) = viewModelScope.launch {
        database.dao.deleteTheme(id)
    }



    fun getScalesOneTheme(id_theme: Int): Flow<List<Scales>> {
        return database.dao.allScalesOneTheme(id_theme)
    }
    fun addNewScale(id_theme: Int, name_scale: String, color: String, type: String) = viewModelScope.launch {
        database.dao.addScale(id_theme, name_scale, color, type)
    }
    fun addNewScale(id_theme: Int, name_scale: String, color: String, type: String, maxCount: Int) = viewModelScope.launch {
        database.dao.addScale(id_theme, name_scale, color, type)
        var id = database.dao.oneScaleCounterType(id_theme, name_scale, color)
        database.dao.addCounter(id,0, maxCount)
    }
    fun deleteScale(id: Int) = viewModelScope.launch {
        database.dao.deleteScale(id)
    }
    fun changeScale(id: Int, name_scale: String, color: String) = viewModelScope.launch {
        database.dao.changeScale(id, name_scale, color)
    }

    fun getCounter(id_scale: Int): Flow<List<Counter>>{
        return database.dao.counterInsideScale(id_scale)
    }
    fun addNewCounter(id_scale: Int, current_count: Int, max_count: Int)=viewModelScope.launch {
        database.dao.addCounter(id_scale, current_count, max_count)
    }
    fun updateCurrentCount(id: Int, current_count: Int)=viewModelScope.launch {
        database.dao.updateCurrentCount(id, current_count)
    }
    fun changeMaxCount(id: Int, max_count: Int) = viewModelScope.launch {
        database.dao.changeMaxCount(id, max_count)
    }

    fun getCheckList(id_scale: Int): Flow<List<CheckList>>{
        return database.dao.allCheckListOneScale(id_scale)
    }
    fun addNewCheckList(id_scale: Int, text: String)=viewModelScope.launch {
        database.dao.addCheckList(id_scale, text)
    }
    fun changeDoneCheckList(id: Int, done: Int)=viewModelScope.launch {
        database.dao.changeDoneCheckList(id, done)
    }
    fun changeTextCheckList(id: Int, text: String)=viewModelScope.launch {
        database.dao.changeTextCheckList(id, text)
    }
    fun deleteCheckList(id: Int)=viewModelScope.launch {
        database.dao.deleteCheckList(id)
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