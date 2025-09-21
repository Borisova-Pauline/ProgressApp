package com.tomli.progressapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.progressapp.databases.ProgressViewModel
import com.tomli.progressapp.databases.Themes
import com.tomli.progressapp.ui.theme.ProgressAppTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemesScreen(navController: NavController, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val themes = progressViewModel.themes.collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isCreate = remember{ mutableStateOf(false)}
    val isChange=remember{ mutableStateOf(false)}
    val changingTheme = remember { mutableStateOf(Themes(0, "name", "color")) }
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Box(modifier = Modifier.fillMaxWidth().background(Color.Black)){
            Image(painter = painterResource(R.drawable.button_more), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterStart))
            Image(painter = painterResource(R.drawable.button_add), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterEnd)
                .clickable{
                    isCreate.value=true
                })
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(horizontal = 2.dp)){
            items(items = themes.value) { item ->
                Column(modifier = Modifier
                    .padding(5.dp).combinedClickable(enabled = true, onClick = {
                        navController.navigate("scales_screen/${item.id}/${item.name_theme}/${item.color}")
                    },onLongClick = {
                        isChange.value=true
                        changingTheme.value=item.copy()
                    })){
                    Box(modifier = Modifier.background(Color(ColorsData.valueOf(item.color!!).hex)).fillMaxWidth().height(80.dp)) //item.color?.toInt(16)!!)
                    Text(text = "${item.name_theme}", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        if(isCreate.value){
            ThemeDialog(Themes(0, "Новая тема", "Red"), false, {isCreate.value=false}, false, { _, _ ->  })
        }
        if(isChange.value){
            ThemeDialog(changingTheme.value, true, {isChange.value=false}, false, {_, _ ->  })
        }
    }
}

//создание и редактирование
@Composable
fun ThemeDialog(theme: Themes, isChange: Boolean, onDismiss:()-> Unit, isChangeInside: Boolean, onReturnChanges:(newName: String, newColor: String)-> Unit, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val name = remember { mutableStateOf(TextFieldValue(theme.name_theme!!)) }
    val colorIndex = remember { mutableStateOf(ColorsData.valueOf(theme.color!!).ordinal) }
    val heightCard: Int
    if(isChange && !isChangeInside){
        heightCard=350
    }else{
        heightCard=300
    }
    val isDelete= remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.width(400.dp).height(heightCard.dp).padding(16.dp), shape = RoundedCornerShape(5.dp)){
            if(!isChange){
                Text(text="Создание новой темы", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
            }else{
                Text(text="Редактирование темы", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
            }
            Row(modifier = Modifier.padding(start=10.dp, end=10.dp)){
                Text(text="Название: ", modifier = Modifier.align(Alignment.CenterVertically))
                OutlinedTextField(value = name.value, onValueChange = {newText -> name.value = newText}, modifier = Modifier, singleLine = true,)
            }
            Row(modifier = Modifier.padding(10.dp)){
                Text(text="Цвет:  ", modifier = Modifier.align(Alignment.CenterVertically))
                LazyVerticalGrid(columns = GridCells.Adaptive(50.dp)) {
                    items(items = ColorsData.entries.toTypedArray()){ item ->
                        Box(modifier = Modifier.background(Color(item.hex)).size(30.dp)
                            .clickable { colorIndex.value= item.ordinal}){
                            if(colorIndex.value==item.ordinal){
                                Image(painter = painterResource(R.drawable.button_add), contentDescription = "", modifier = Modifier.align(
                                    Alignment.Center).padding(5.dp))
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable { onDismiss() }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                    Text(text="Отменить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
                if(!isChange){
                    Card(modifier = Modifier.weight(1f).padding(5.dp)
                        .clickable {
                            progressViewModel.addNewTheme(name.value.text, ColorsData.entries.get(colorIndex.value).name)
                            onDismiss()
                        }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                        Text(text="Создать", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                    }
                }else{
                    Card(modifier = Modifier.weight(1f).padding(5.dp)
                        .clickable {
                            progressViewModel.changeTheme(theme.id!!, name.value.text, ColorsData.entries.get(colorIndex.value).name)
                            if(isChangeInside){
                                onReturnChanges(name.value.text, ColorsData.entries.get(colorIndex.value).name)
                            }
                            onDismiss()
                        }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                        Text(text="Сохранить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                    }
                }
            }
            if(isChange && !isChangeInside){
                Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                    Card(modifier = Modifier.weight(1f).padding(5.dp)
                        .clickable { isDelete.value=true }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                        Text(text="Удалить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                    }
                }
            }
            if(isDelete.value){
                AlertDialog(onDismissRequest = {isDelete.value=false},
                    title = {Text(text="Удалить тему?")},
                    text = {Text(text="Все записи внутри неё будут также удалены")},
                    confirmButton = {Text(text = "Удалить", modifier = Modifier.padding(5.dp)
                        .clickable { progressViewModel.deleteTheme(theme.id!!); isDelete.value=false; onDismiss()})},
                    dismissButton = {Text(text = "Отменить", modifier = Modifier.padding(5.dp)
                        .clickable { isDelete.value=false})})
            }
        }
    }
}




enum class ColorsData(val hex: Long){
    Red(0xffff0000), Orange(0xffff8000), Yellow(0xFFffe500),
    Green(0xff23c905), Cyan(0xff00d0ff), Blue(0xff004cff),
    Purple(0xff9400ff), Pink(0xffff00a1), Brown(0xff77492b),
    Black(0xff000000), Grey(0xff6e6e6e);
}