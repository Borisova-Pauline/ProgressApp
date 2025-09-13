package com.tomli.progressapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
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
import com.tomli.progressapp.ui.theme.ProgressAppTheme


@Composable
fun ThemesScreen(navController: NavController, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val themes = progressViewModel.themes.collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isCreate = remember{ mutableStateOf(false)}
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Box(modifier = Modifier.fillMaxWidth().background(Color.Black)){
            Image(painter = painterResource(R.drawable.button_more), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterStart))
            Image(painter = painterResource(R.drawable.button_add), contentDescription = "", modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.CenterEnd)
                .clickable{
                    isCreate.value=true
                    //progressViewModel.addNewTheme("theme", "0")
                })
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(2.dp)){
            items(items = themes.value) { item ->
                Column(modifier = Modifier
                    .padding(5.dp).clickable {}){
                    Box(modifier = Modifier.background(Color(ColorsData.valueOf(item.color!!).hex)).fillMaxWidth().height(80.dp)) //item.color?.toInt(16)!!)
                    Text(text = "${item.name_theme}", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        if(isCreate.value){
            ThemeCreateDialog({isCreate.value=false})
        }

    }
}


@Composable
fun ThemeCreateDialog(onDismiss:()-> Unit, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    var name = remember { mutableStateOf(TextFieldValue("Новая тема")) }
    var colorIndex = remember { mutableStateOf(0) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.width(400.dp).height(300.dp).padding(16.dp), shape = RoundedCornerShape(5.dp)){
            Text(text="Создание новой темы", modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp), fontSize = 20.sp)
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
            Row(modifier = Modifier.padding(10.dp).height(200.dp).fillMaxWidth()){
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable { onDismiss() }, shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                    Text(text="Отменить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable {
                        progressViewModel.addNewTheme(name.value.text, ColorsData.entries.get(colorIndex.value).name)
                        onDismiss()
                    }, shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)){
                    Text(text="Создать", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProgressAppTheme {
        ThemeCreateDialog({})
    }
}



enum class ColorsData(val hex: Long){
    Red(0xffff0000), Orange(0xffff8000), Yellow(0xFFffe500),
    Green(0xff23c905), Cyan(0xff00d0ff), Blue(0xff004cff),
    Purple(0xff9400ff), Pink(0xffff00a1), Brown(0xff77492b),
    Black(0xff000000), Grey(0xff6e6e6e);
}