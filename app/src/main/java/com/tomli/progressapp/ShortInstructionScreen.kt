package com.tomli.progressapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ShortInstructionScreen(navController: NavController){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val appName = stringResource(id=R.string.app_name)


    val currentCount = remember { mutableStateOf(0) }
    val maxCount = remember { mutableStateOf(10) }
    val isCheck = remember { mutableStateOf(false) }
    var progress1 = remember { mutableStateOf(0.0f) }

    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
            Image(painter = painterResource(R.drawable.button_back),
                contentDescription = "",
                modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterStart
                )
                    .clickable {navController.navigate("main_screen") })
            Text(
                text = "Краткая инструкция",
                color = Color.White,
                modifier = Modifier.padding(10.dp).align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        Column(modifier = Modifier.padding(horizontal = 10.dp).verticalScroll(rememberScrollState())) {
            Text(text = "Приложение $appName имеет 4 типа элементов: темы, шкалы, чек-листв и счётчики. На главном экране находятся все ваши темы.",
                textAlign = TextAlign.Justify)
            Column(modifier = Modifier
                .padding(15.dp).align(Alignment.CenterHorizontally)){
                Box(modifier = Modifier.background(Color.Black, shape = RoundedCornerShape(9.dp)).height(80.dp).width(120.dp)){
                    Box(modifier = Modifier.align(Alignment.Center)){
                        OutlinedText("${(((progress1.value+(currentCount.value.toFloat()/maxCount.value.toFloat()))/2)*100).toInt()}%", "Black", 24)
                    }
                }
                Text(text = "Пример темы", modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center)
            }
            Text(text = "Нажатием на «+» вы можете создать новую тему, долгим нажатием на существующую тему откроется окно, в котором вы можете редактировать тему или удалить её.",
                textAlign = TextAlign.Justify)
            Text(text="Учтите, удаление элемента также удаляет другие элементы, находящиеся внутри.",
                textAlign = TextAlign.Justify)
            Box(modifier = Modifier.padding(top = 15.dp).fillMaxWidth().height(50.dp)){
                LinearProgressIndicator(progress = (progress1.value), color = Color.Black, trackColor = Color(ColorsData.Black.lightHex), modifier = Modifier.fillMaxWidth().height(50.dp)/*.border(1.dp, color = Color(ColorsData.valueOf(color).darkHex))*/)
                Box(modifier = Modifier.align(Alignment.Center)){
                    OutlinedText("${(progress1.value*100).toInt()}%", "Black", 19)
                }
            }
            Text(text="Пример шкалы 1", modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp),
                textAlign = TextAlign.Center)
            Box(modifier = Modifier.padding(top = 15.dp).fillMaxWidth().height(50.dp)){
                LinearProgressIndicator(progress = (currentCount.value.toFloat()/maxCount.value.toFloat()), color = Color.Black, trackColor = Color(ColorsData.Black.lightHex), modifier = Modifier.fillMaxWidth().height(50.dp))
                Box(modifier = Modifier.align(Alignment.Center)){
                    OutlinedText("${((currentCount.value.toFloat()/maxCount.value.toFloat())*100).toInt()}%", "Black", 19)
                }
            }
            Text(text="Пример шкалы 2", modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                textAlign = TextAlign.Center)
            Text(text="Внутри темы располагаются шкалы. У них может быть один из следующих типов: «чек-лист» и «счётчик». Тип шкалы нельзя изменить при редактировании.",
                textAlign = TextAlign.Justify)
            Text(text = "С типом «чек-лист» шкала имеет внутри изначально пустой список. Нажатием на «+» вы можете добавлять пункты. Их содержание можно редактировать нажатием на текст.",
                textAlign = TextAlign.Justify)
            Row(modifier = Modifier.fillMaxWidth().padding(6.dp)){
                Checkbox(checked = isCheck.value, onCheckedChange = {
                    isCheck.value=!isCheck.value
                    if(isCheck.value){
                        progress1.value = 1.0f
                    }else{
                        progress1.value = 0.0f
                    }
                }, colors = CheckboxDefaults.colors(checkedColor = Color.Black, uncheckedColor = Color.Black, checkmarkColor = Color.White))
                Text(text="Пример пункта в чек-листе", modifier = Modifier.align(Alignment.CenterVertically))
            }
            Text(text="Для отметки выполнения пункта, нажмите на чек-бокс рядом с текстом.")
            Text(text="Тип «счётчик» при создании шкалы автоматически создаёт счётчик внутри шкалы.")
            Column(modifier = Modifier.padding(15.dp).fillMaxWidth()){
                Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp)){
                    Text(text = "  -  ", fontSize = 35.sp, modifier = Modifier.clickable {
                        if(currentCount.value>0){
                            currentCount.value--
                        }
                    }.align(Alignment.CenterVertically))
                    Text(text = "${currentCount.value}/${maxCount.value}", fontSize = 30.sp, modifier = Modifier.align(Alignment.CenterVertically))
                    Text(text = "  +  ", fontSize = 35.sp, modifier = Modifier.clickable {
                        if(currentCount.value<maxCount.value){
                            currentCount.value++
                        }
                    }.align(Alignment.CenterVertically))
                }
            }
            Text(text="Удалить его можно только вместе со шкалой. На «+» и «-»  к текущему счёту прибавляется и убавляется по единице. При нажатии на счёт открывается окно, дающее возможность редактировать как текущий, так и максимальный счёт.",
                textAlign = TextAlign.Justify)
            Text(text="На этом инструкция завершается.", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text(text="Приятного пользования!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}



/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProgressAppTheme {
        ShortInstructionScreen(navController = rememberNavController())
    }
}*/