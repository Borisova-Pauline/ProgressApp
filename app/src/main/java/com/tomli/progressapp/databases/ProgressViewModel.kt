package com.tomli.progressapp.databases

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tomli.progressapp.Applic
import com.tomli.progressapp.TypeScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
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

    var _progress = MutableStateFlow(0.0f)
    var progress: StateFlow<Float> = _progress
    fun howMuchChecked(id_scale: Int)=viewModelScope.launch {
        val checked = database.dao.howMuchChecked(id_scale)
        val allCheckLists = database.dao.howMuchInCheckList(id_scale)
        _progress.value=checked.toFloat()/allCheckLists.toFloat()
    }


    fun howMuchCheckedShow(id_scale: Int, type: String, onReturn:(ret: StateFlow<Float>)-> Unit)=viewModelScope.launch {
        var _prog = MutableStateFlow(0.0f)
        var prog: StateFlow<Float> = _prog
        if(type==TypeScale.CheckList.name){
            val checked = database.dao.howMuchChecked(id_scale)
            val allCheckLists = database.dao.howMuchInCheckList(id_scale)
            _prog.value=checked.toFloat()/allCheckLists.toFloat()
        }else{
            val current = database.dao.getCountIntCurrent(id_scale)
            val max = database.dao.getCountIntMax(id_scale)
            _prog.value=current.toFloat()/max.toFloat()
        }
        onReturn(prog)
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
