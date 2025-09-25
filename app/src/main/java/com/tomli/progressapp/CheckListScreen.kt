package com.tomli.progressapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.progressapp.databases.CheckList
import com.tomli.progressapp.databases.ProgressViewModel
import com.tomli.progressapp.databases.Scales

@Composable
fun CheckListScreen(navController: NavController, id: Int, name: String, color: String, progressViewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory)) {
    var name_scale = remember { mutableStateOf(name) }
    var color_scale= remember { mutableStateOf(color) }
    val check_list = progressViewModel.getCheckList(id).collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isScaleChange = remember { mutableStateOf(false) }
    progressViewModel.howMuchChecked(id)
    val isCheckCreate = remember { mutableStateOf(false) }
    val isCheckChange = remember { mutableStateOf(false) }
    var changingCheck = CheckList(0, id, 0, "")
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Row(modifier = Modifier.fillMaxWidth().background(Color(ColorsData.valueOf(color_scale.value).hex)).height(60.dp)){
            if(isLightColor(color_scale.value)){
                Image(painter = painterResource(R.drawable.button_back_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.Black,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Center)

                Image(painter = painterResource(R.drawable.button_change_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
                Image(painter = painterResource(R.drawable.button_add_black), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isCheckCreate.value=true
                    })
            }else{
                Image(painter = painterResource(R.drawable.button_back), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.White,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Center)

                Image(painter = painterResource(R.drawable.button_change), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
                Image(painter = painterResource(R.drawable.button_add), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isCheckCreate.value=true
                        //progressViewModel.addNewCheckList(id, "text")
                    })
            }
        }
        val progress = progressViewModel.progress.collectAsState()
        LinearProgressIndicator(progress = (progress.value), color = Color(ColorsData.valueOf(color_scale.value).hex), modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp))
        Text(text = "${(progress.value*100).toInt()}%", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        LazyVerticalGrid(columns = GridCells.Fixed(1),modifier = Modifier.padding(horizontal = 2.dp)){
            items(items = check_list.value) { item ->
                val isCheck = remember { mutableStateOf(!changeCheckBool(item.done!!)) }
                progressViewModel.howMuchChecked(id)
                Row(modifier = Modifier.fillMaxWidth().padding(6.dp)){
                    Checkbox(checked = isCheck.value, onCheckedChange = {
                        isCheck.value=changeCheckBool(item.done!!)
                        progressViewModel.changeDoneCheckList(item.id!!, changeCheckInt(!isCheck.value))
                        progressViewModel.howMuchChecked(id)
                                                                        }, colors = CheckboxDefaults.colors(checkedColor = Color.Black, uncheckedColor = Color.Black))
                    Text(text=item.text!!, modifier = Modifier.align(Alignment.CenterVertically).clickable{
                        changingCheck = item.copy()
                        isCheckChange.value=true
                    })
                }
            }
        }
        if(isScaleChange.value){
            ScaleDialog(Scales(id, 0, name_scale.value, color_scale.value, TypeScale.Counter.name), true, {isScaleChange.value = false}, true, {newName, newColor ->  name_scale.value=newName; color_scale.value=newColor})
        }
        if(isCheckCreate.value){
            CheckListDialog(CheckList(0, id, 0, ""), false, {isCheckCreate.value=false})
        }
        if(isCheckChange.value){
            CheckListDialog(changingCheck, true, {isCheckChange.value=false})
        }
    }
}


fun changeCheckBool(check: Int): Boolean{
    if(check==0){
        return true
    }else{
        return false
    }
}


fun changeCheckInt(check: Boolean): Int{
    if(check){
        return 0
    }else{
        return 1
    }
}


@Composable
fun CheckListDialog(checkList: CheckList, isChange: Boolean, onDismiss:()-> Unit, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    Dialog(onDismiss) {
        Card(modifier = Modifier.width(400.dp).height(300.dp).padding(16.dp), shape = RoundedCornerShape(5.dp)){
            if(!isChange){
                Text(text="Добавление пункта", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
            }else{
                Text(text="Редактирование пункта", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
            }
        }
    }
}



enum class DoneCheckList(val done: Boolean){
    NotDone(false), Done(true);
}