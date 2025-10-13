package com.tomli.progressapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ShortInstructionScreen(navController: NavController){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
        Text("[Не доделано]")
    }
}