package com.tomli.progressapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)){
        Row(modifier = Modifier.fillMaxWidth().background(Color(ColorsData.valueOf(color_scale.value).hex)).height(60.dp)){
            if(isLightColor(color_scale.value)){
                Image(painter = painterResource(R.drawable.button_back_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.Black,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Center)

                Image(painter = painterResource(R.drawable.button_change_black), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
            }else{
                Image(painter = painterResource(R.drawable.button_back), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterVertically).clickable { navController.navigateUp() })

                Text(text = name_scale.value, color = Color.White,
                    modifier = Modifier.weight(1f).padding(10.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Center)

                Image(painter = painterResource(R.drawable.button_change), contentDescription = "", modifier = Modifier.size(55.dp).padding(10.dp).align(Alignment.CenterVertically)
                    .clickable{
                        isScaleChange.value=true
                    })
            }

        }
        LazyVerticalGrid(columns = GridCells.Fixed(1),modifier = Modifier.padding(horizontal = 2.dp)){
            items(items = counter.value) { item ->
                Column(modifier = Modifier
                    .padding(5.dp).fillMaxWidth().combinedClickable(enabled = true, onClick = {

                    },onLongClick = {

                    })){
                    Text(text = "${item.current_count}/${item.max_count}", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        if(isScaleChange.value){
            ScaleDialog(Scales(id, 0, name_scale.value, color_scale.value, TypeScale.Counter.name), true, {isScaleChange.value = false}, true, {newName, newColor ->  name_scale.value=newName; color_scale.value=newColor})
        }
    }
}