package com.tomli.progressapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.progressapp.databases.ProgressViewModel
import com.tomli.progressapp.databases.Scales
import com.tomli.progressapp.databases.Themes
import com.tomli.progressapp.ui.theme.ProgressAppTheme
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScalesScreen(navController: NavController, id: Int, name: String, color: String, progressViewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory)) {
    var name_theme= remember { mutableStateOf(name) }
    var color_theme= remember { mutableStateOf(color) }
    val scales = progressViewModel.getScalesOneTheme(id).collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isThemeChange = remember{ mutableStateOf(false)}
    val isCreate = remember{ mutableStateOf(false)}
    val isChange=remember{ mutableStateOf(false)}
    val changingScale = remember { mutableStateOf(Scales(0, id, "Новая шкала", "Red", TypeScale.CheckList.name)) }
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Row(modifier = Modifier.fillMaxWidth().background(Color(ColorsData.valueOf(color_theme.value).hex)).height(60.dp)){
            if(isLightColor(color_theme.value)){
                Image(painter = painterResource(R.drawable.button_back_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigate("main_screen") })

                Text(text = name_theme.value, color = Color.Black, lineHeight = 18.sp,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Image(painter = painterResource(R.drawable.button_change_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isThemeChange.value=true
                    })

                Image(painter = painterResource(R.drawable.button_add_black), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isCreate.value=true
                    })
            }else{
                Image(painter = painterResource(R.drawable.button_back), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigate("main_screen") })

                Text(text = name_theme.value, color = Color.White, lineHeight = 18.sp,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Image(painter = painterResource(R.drawable.button_change), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isThemeChange.value=true
                    })

                Image(painter = painterResource(R.drawable.button_add), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isCreate.value=true
                    })
            }

        }
        if(scales.value.isEmpty()){
            Box(modifier = Modifier.weight(1f).padding(20.dp)){
                Text(text="Тема пустая\nНажмите на +, чтобы добавить новую шкалу",
                    fontSize = 20.sp, textAlign = TextAlign.Center, color = Color.Gray,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center))
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(1),modifier = Modifier.padding(horizontal = 2.dp)){
            items(items = scales.value) { item ->
                Column(modifier = Modifier
                    .padding(5.dp).fillMaxWidth().combinedClickable(enabled = true, onClick = {
                        if(item.type==TypeScale.Counter.name){
                            navController.navigate("counter_screen/${item.id}/${item.name_scale}/${item.color}")
                        }else{
                            navController.navigate("check_list_screen/${item.id}/${item.name_scale}/${item.color}")
                        }
                    },onLongClick = {
                        changingScale.value = item.copy()
                        isChange.value=true
                    })){

                    ProgressScale(item.id!!, item.color!!, item.type!!)

                    Text(text = "${item.name_scale}", modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center)
                }
            }
        }
        if(isThemeChange.value){
            ThemeDialog(Themes(id, name_theme.value, color_theme.value), true, {isThemeChange.value=false}, true, {newName, newColor ->  name_theme.value=newName; color_theme.value=newColor})
        }
        if(isCreate.value){
            ScaleDialog(Scales(0, id,"Новая шкала", "Red", TypeScale.CheckList.name), false, {isCreate.value=false}, false, {_, _ ->  })
        }
        if(isChange.value){
            ScaleDialog(changingScale.value, true, {isChange.value=false}, false, {_, _ ->  })
        }
    }
}


@Composable
fun ScaleDialog(scale: Scales, isChange: Boolean, onDismiss:()-> Unit, isChangeInside: Boolean, onReturnChanges:(newName: String, newColor: String)-> Unit, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val name = remember { mutableStateOf(TextFieldValue(scale.name_scale!!)) }
    val colorIndex = remember { mutableStateOf(ColorsData.valueOf(scale.color!!).ordinal) }
    val heightCard: Int
    val isDelete= remember { mutableStateOf(false) }
    val isCounter = remember { mutableStateOf(false) }
    val maxCount = remember { mutableStateOf("10") }
    if(isChange){
        if(isChangeInside){
            heightCard=300
        }else{
            heightCard=350
        }
    } else{
        if(isCounter.value){
            heightCard=450
        }else{
            heightCard=400
        }
    }
    Dialog(onDismiss) {
        Card(modifier = Modifier.width(400.dp).height(heightCard.dp).padding(16.dp), shape = RoundedCornerShape(5.dp)){
            if(!isChange){
                Text(text="Добавление новой шкалы", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
            }else{
                Text(text="Редактирование шкалы", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
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
                                val buttonImage: Int
                                if(isLightColor(item.name)){
                                    buttonImage=R.drawable.button_add_black
                                }else{
                                    buttonImage=R.drawable.button_add
                                }
                                Image(painter = painterResource(buttonImage), contentDescription = "", modifier = Modifier.align(
                                    Alignment.Center).padding(5.dp))
                            }
                        }
                    }
                }
            }
            if(!isChange){
                Row(modifier= Modifier.padding(horizontal = 10.dp)){
                    Text(text="Тип:  ", modifier = Modifier.align(Alignment.CenterVertically))
                    Column{
                        Row{
                            Checkbox(checked = !isCounter.value, onCheckedChange = {isCounter.value=!isCounter.value}, colors = CheckboxDefaults.colors(checkedColor = Color.Black, uncheckedColor = Color.Black))
                            Text(text="Чек-лист", modifier = Modifier.align(Alignment.CenterVertically))
                        }
                        Row{
                            Checkbox(checked = isCounter.value, onCheckedChange = {isCounter.value=!isCounter.value}, colors = CheckboxDefaults.colors(checkedColor = Color.Black, uncheckedColor = Color.Black))
                            Text(text="Счётчик", modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            }
            if(isCounter.value && !isChange){
                Row(modifier = Modifier.padding(start=10.dp, end=10.dp)){
                    Text(text="Максимальное \nколичество: ", modifier = Modifier.align(Alignment.CenterVertically))
                    OutlinedTextField(value = maxCount.value, onValueChange = {newText -> maxCount.value = newText}, modifier = Modifier,
                        singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            }
            Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable { onDismiss() }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)
                ){
                    Text(text="Отменить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
                if(!isChange){
                    Card(modifier = Modifier.weight(1f).padding(5.dp)
                        .clickable {
                            if(isCounter.value){
                                progressViewModel.addNewScale(scale.id_theme!!, name.value.text, ColorsData.entries.get(colorIndex.value).name, TypeScale.Counter.name, maxCount.value.toInt())
                            }else{
                                progressViewModel.addNewScale(scale.id_theme!!, name.value.text, ColorsData.entries.get(colorIndex.value).name, TypeScale.CheckList.name)
                            }
                            onDismiss()
                        }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)
                    ){
                        Text(text="Добавить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                    }
                }else{
                    Card(modifier = Modifier.weight(1f).padding(5.dp)
                        .clickable {
                            progressViewModel.changeScale(scale.id!!, name.value.text, ColorsData.entries.get(colorIndex.value).name)
                            if(isChangeInside){
                                onReturnChanges(name.value.text, ColorsData.entries.get(colorIndex.value).name)
                            }
                            onDismiss()
                        }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)
                    ){
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
                        .clickable { progressViewModel.deleteScale(scale.id!!); isDelete.value=false; onDismiss()})},
                    dismissButton = {Text(text = "Отменить", modifier = Modifier.padding(5.dp)
                        .clickable { isDelete.value=false})})
            }
        }
    }
}





@Composable
fun ProgressScale(id_scale: Int, color: String, type: String, progressViewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory)){
    var progress = remember { mutableStateOf(0.0f) }
    progressViewModel.howMuchCheckedShow(id_scale, type, {ret-> progress.value=ret.value})
    Box(modifier = Modifier.fillMaxWidth().height(50.dp)){
        LinearProgressIndicator(progress = (progress.value), color = Color(ColorsData.valueOf(color).hex), trackColor = Color(ColorsData.valueOf(color).lightHex), modifier = Modifier.fillMaxWidth().height(50.dp)/*.border(1.dp, color = Color(ColorsData.valueOf(color).darkHex))*/)
        Box(modifier = Modifier.align(Alignment.Center)){
            OutlinedText("${(progress.value*100).toInt()}%", color, 19)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProgressAppTheme {
        //ThemeChangeDialog(Themes(0, "n", "Pink"),{})
        //ScaleDialog(Scales(0, 0, "Новая шкала", "Red", TypeScale.CheckList.name), true, {})
    }
}




enum class TypeScale{
    Counter, CheckList
}