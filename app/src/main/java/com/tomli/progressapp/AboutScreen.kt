package com.tomli.progressapp

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomli.progressapp.ui.theme.ProgressAppTheme
import com.tomli.progressapp.ui.theme.PurpleAmetist

@Composable
fun AboutScreen(navController: NavController){
    val context = LocalContext.current
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val appName = stringResource(id=R.string.app_name)
    val appVersion = getAppVersion(context)
    Column(modifier = Modifier.fillMaxSize().padding(top = up, bottom = down)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
            Image(painter = painterResource(R.drawable.button_back),
                contentDescription = "",
                modifier = Modifier.size(55.dp).padding(10.dp).align(
                    Alignment.CenterStart
                )
                    .clickable {navController.navigate("main_screen") })
            Text(
                text = "О приложении",
                color = Color.White,
                modifier = Modifier.padding(10.dp).align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        Column(modifier = Modifier.padding(horizontal = 10.dp)){
            Text(text=appName, modifier = Modifier.fillMaxWidth().padding(top = 15.dp), textAlign = TextAlign.Center,
                fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Версия: $appVersion", modifier = Modifier.fillMaxWidth().padding(top = 7.dp),
                textAlign = TextAlign.Center, color = Color(0xff7b7b7b))
            Image(painter = painterResource(R.mipmap.progress_icon),contentDescription = null,
                modifier = Modifier.size(170.dp).align(Alignment.CenterHorizontally))
            Text(text="Приложение для отслеживания прогресса на основе заполнения чек-листов и счётчиков.", modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                textAlign = TextAlign.Center)
            Text(text = "Нет ограничений на количество создаваемых элементов.", modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                textAlign = TextAlign.Center)
            Text(text = "Краткая инструкция по пользованию", modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                .clickable { navController.navigate("instruction_screen") },
                textAlign = TextAlign.Center, color = PurpleAmetist)
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically){
                Text(text = "Обратная связь: ")
                SelectionContainer {
                    Text(text = "dbpolina48@gmail.com", style = TextStyle(textDecoration = TextDecoration.Underline), fontSize = 18.sp)
                }
            }
        }
    }
}


fun getAppVersion(context: Context): String {
    try {
        val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName!!
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return "Неизвестно"
    }
}
