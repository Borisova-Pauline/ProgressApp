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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.progressapp.databases.ProgressViewModel
import com.tomli.progressapp.databases.Themes
import com.tomli.progressapp.ui.theme.ProgressAppTheme
import kotlinx.coroutines.launch
import java.util.Base64


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemesScreen(navController: NavController, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val themes = progressViewModel.themes.collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isCreate = remember{ mutableStateOf(false)}
    val isChange=remember{ mutableStateOf(false)}
    val changingTheme = remember { mutableStateOf(Themes(0, "name", "color")) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val appName = stringResource(id=R.string.app_name)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                Column(modifier =Modifier.padding(horizontal = 10.dp).verticalScroll(rememberScrollState())){
                    Row(modifier = Modifier.height(70.dp).padding(vertical = 12.dp)){
                        Image(painter = painterResource(R.mipmap.progress_icon), contentDescription = null,
                            modifier = Modifier.padding(5.dp))
                        Box(modifier = Modifier.weight(1f).align(Alignment.CenterVertically)){
                            Text(text=appName, fontSize = 22.sp, modifier = Modifier.padding(7.dp))
                        }
                    }
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Row{
                                Image(painter = painterResource(R.drawable.button_add_black), contentDescription = null,
                                    modifier = Modifier.padding(5.dp).size(25.dp))
                                Box(modifier = Modifier.weight(1f).padding(horizontal = 5.dp).align(Alignment.CenterVertically)){
                                    Text("Новая тема", fontSize = 20.sp, color=Color.Black)
                                }
                            }
                        },
                        selected = false,
                        onClick = {scope.launch { drawerState.close()}; isCreate.value=true},
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White
                        )
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Row{
                                Image(painter = painterResource(R.drawable.button_instruction), contentDescription = null,
                                    modifier = Modifier.padding(5.dp).size(25.dp))
                                Box(modifier = Modifier.weight(1f).padding(horizontal = 5.dp).align(Alignment.CenterVertically)){
                                    Text("Краткая инструкция", fontSize = 20.sp, color=Color.Black)
                                }
                            }
                        },
                        selected = false,
                        onClick = {},
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White
                        )
                    )
                    NavigationDrawerItem(
                        label = {
                            Row{
                                Image(painter = painterResource(R.drawable.button_about), contentDescription = null,
                                    modifier = Modifier.padding(5.dp).size(25.dp))
                                Box(modifier = Modifier.weight(1f).padding(horizontal = 5.dp).align(Alignment.CenterVertically)){
                                    Text("О приложении", fontSize = 20.sp, color=Color.Black)
                                }
                            }
                        },
                        selected = false,
                        onClick = {},
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White
                        )
                    )
                    NavigationDrawerItem(
                        label = {
                            Row{
                                Image(painter = painterResource(R.drawable.button_share), contentDescription = null,
                                    modifier = Modifier.padding(5.dp).size(25.dp))
                                Box(modifier = Modifier.weight(1f).padding(horizontal = 5.dp).align(Alignment.CenterVertically)){
                                    Text("Поделиться", fontSize = 20.sp, color=Color.Black)
                                }
                            }
                        },
                        selected = false,
                        onClick = {},
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White
                        )
                    )
                }
            }
        }, drawerState=drawerState
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
            Box(modifier = Modifier.fillMaxWidth().background(Color.Black)){
                Image(painter = painterResource(R.drawable.button_more), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterStart)
                    .clickable { scope.launch {
                        if(drawerState.isClosed){
                            drawerState.open()
                        }else{
                            drawerState.close()
                        }
                    } })
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
                        Box(modifier = Modifier.background(Color(ColorsData.valueOf(item.color!!).hex), shape = RoundedCornerShape(9.dp)).fillMaxWidth().height(80.dp)){
                            var progress = remember { mutableStateOf(0.0f) }
                            progressViewModel.progressTheme(item.id!!, {ret-> progress.value=ret.value})
                            Box(modifier = Modifier.align(Alignment.Center)){
                                OutlinedText("${(progress.value*100).toInt()}%", item.color, 24)
                            }
                        }
                        Text(text = "${item.name_theme}", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    }
                }
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


@Composable
fun OutlinedText(text: String, color: String, textSize: Int){
    Text(text=text, style= TextStyle.Default.copy(color=Color(ColorsData.valueOf(color).darkHex), fontSize = textSize.sp,
        drawStyle = Stroke(miter = 10f, width = 18f, join = StrokeJoin.Round))
    )
    Text(text=text, color = Color.White,
        style= TextStyle.Default.copy(color=Color(ColorsData.valueOf(color).darkHex), fontSize = textSize.sp)
    )
}



enum class ColorsData(val hex: Long, val lightHex: Long, val darkHex: Long){
    Red(0xffff0000, 0xffffdddd, 0xff540000),
    Orange(0xffff8000, 0xffffe9d4, 0xff5a2a00),
    Yellow(0xFFffe500, 0xfffffae9, 0xff5b5100),
    Green(0xff23c905, 0xffd3ffca, 0xff0b4200),
    Cyan(0xff00d0ff, 0xffcff6ff, 0xff004452),
    Blue(0xff004cff, 0xffd6e3ff, 0xff001543),
    Purple(0xff9400ff, 0xfff0ddff, 0xff28004c),
    Pink(0xffff00a1, 0xffffd3ee, 0xff510030),
    Brown(0xff77492b, 0xffdaccc3, 0xff432009),
    Black(0xff000000, 0xffc8c8c8, 0xff000000),
    Grey(0xff6e6e6e, 0xffe4e4e4, 0xff191919);
}