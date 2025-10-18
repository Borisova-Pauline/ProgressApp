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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.progressapp.databases.Counter
import com.tomli.progressapp.databases.ProgressViewModel
import com.tomli.progressapp.databases.Scales

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CounterScreen(navController: NavController, id: Int, name: String, color: String, progressViewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory)){
    var name_scale = remember { mutableStateOf(name) }
    var color_scale=remember { mutableStateOf(color) }
    val counter = progressViewModel.getCounter(id).collectAsState(initial = emptyList())
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isScaleChange = remember { mutableStateOf(false) }
    val isCounterChange = remember { mutableStateOf(false) }
    val counterChange = remember { mutableStateOf(Counter(0, 0, 0, 0)) }
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Row(modifier = Modifier.fillMaxWidth().background(Color(ColorsData.valueOf(color_scale.value).hex)).height(60.dp)){
            if(isLightColor(color_scale.value)){
                Image(painter = painterResource(R.drawable.button_back_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.Black,lineHeight = 18.sp,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Image(painter = painterResource(R.drawable.button_change_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
            }else{
                Image(painter = painterResource(R.drawable.button_back), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.White,lineHeight = 18.sp,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)

                Image(painter = painterResource(R.drawable.button_change), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
            }

        }
        LazyVerticalGrid(columns = GridCells.Fixed(1),modifier = Modifier.padding(horizontal = 2.dp)){
            items(items = counter.value) { item ->
                Column(modifier = Modifier
                    .padding(15.dp).fillMaxWidth()){
                    LinearProgressIndicator(progress = (item.current_count!!.toFloat()/item.max_count!!.toFloat()), color = Color(ColorsData.valueOf(color_scale.value).hex), trackColor = Color(ColorsData.valueOf(color).lightHex), modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp))

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp)){
                        Text(text = "  -  ", fontSize = 35.sp, modifier = Modifier.clickable {
                            if(item.current_count!!>0){
                                progressViewModel.updateCurrentCount(item.id!!, item.current_count-1)
                            }
                        }.align(Alignment.CenterVertically))
                        Text(text = "${item.current_count}/${item.max_count}", fontSize = 30.sp, modifier = Modifier.clickable {
                            counterChange.value = item.copy()
                            isCounterChange.value=true
                        }.align(Alignment.CenterVertically))
                        Text(text = "  +  ", fontSize = 35.sp, modifier = Modifier.clickable {
                            if(item.current_count!!<item.max_count!!){
                                progressViewModel.updateCurrentCount(item.id!!, item.current_count+1)
                            }
                        }.align(Alignment.CenterVertically))
                    }
                }
            }
        }
        if(isScaleChange.value){
            ScaleDialog(Scales(id, 0, name_scale.value, color_scale.value, TypeScale.Counter.name), true, {isScaleChange.value = false}, true, {newName, newColor ->  name_scale.value=newName; color_scale.value=newColor})
        }
        if(isCounterChange.value){
            CounterDialog(counterChange.value, {isCounterChange.value=false})
        }
    }
}


@Composable
fun CounterDialog(counter: Counter, onDismiss:()-> Unit, progressViewModel: ProgressViewModel= viewModel(factory = ProgressViewModel.factory)){
    val newCurrentCount = remember { mutableStateOf(counter.current_count.toString()) }
    val newMaxCount = remember { mutableStateOf(counter.max_count.toString()) }
    val context = LocalContext.current
    Dialog(onDismiss){
        Card(modifier = Modifier.width(400.dp).height(270.dp).padding(16.dp), shape = RoundedCornerShape(5.dp)){
            Text(text="Редактирование счётчика", modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp), fontSize = 20.sp)
            Row(modifier = Modifier.padding(start=10.dp, end=10.dp)){
                Text(text="Текущее \nколичество: ", modifier = Modifier.align(Alignment.CenterVertically))
                OutlinedTextField(value = newCurrentCount.value, onValueChange = {newText -> newCurrentCount.value = newText}, modifier = Modifier,
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.padding(start=10.dp, end=10.dp)){
                Text(text="Максимальное \nколичество: ", modifier = Modifier.align(Alignment.CenterVertically))
                OutlinedTextField(value = newMaxCount.value, onValueChange = {newText -> newMaxCount.value = newText}, modifier = Modifier,
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable { onDismiss() }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)
                ){
                    Text(text="Отменить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
                Card(modifier = Modifier.weight(1f).padding(5.dp)
                    .clickable {
                        try{
                            if(newCurrentCount.value.toInt()<=newMaxCount.value.toInt() && newCurrentCount.value.toInt()>=0 && newMaxCount.value.toInt()>0){
                                progressViewModel.updateCurrentCount(counter.id!!, newCurrentCount.value.toInt())
                                progressViewModel.changeMaxCount(counter.id!!, newMaxCount.value.toInt())
                                onDismiss()
                            }else{
                                var mistake = "Неправильные изменения"
                                when{
                                    newCurrentCount.value.toInt()>=newMaxCount.value.toInt() -> mistake="Текущий счёт не может быть больше максимального"
                                    newMaxCount.value.toInt()<0 -> mistake="Максимальный счёт должен быть больше нуля"
                                    newCurrentCount.value.toInt()<=0 -> mistake="Текущий счёт должен быть равен или больше 0"
                                }
                                Toast.makeText(context, mistake, Toast.LENGTH_LONG).show()
                            }
                        }catch (e: Exception){
                            Toast.makeText(context, "Введите числа", Toast.LENGTH_LONG).show()
                        }

                    }.align(Alignment.CenterVertically), shape = RoundedCornerShape(5.dp), border = BorderStroke(1.dp, Color.Black)
                ){
                    Text(text="Сохранить", modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}