package com.tomli.progressapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tomli.progressapp.databases.Themes
import com.tomli.progressapp.ui.theme.ProgressAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgressAppTheme {
                ComposeNavigation()
            }
        }
    }
}

fun isLightColor(color: String): Boolean{
    var isLight = false
    if(color=="Yellow"){
        isLight=true
    }
    return isLight
}


@Composable
fun ComposeNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") {
            ThemesScreen(navController = navController)
        }
        composable("scales_screen/{id}/{name}/{color}", arguments = listOf(navArgument("id") {type = NavType.IntType},
            navArgument("name") {type = NavType.StringType}, navArgument("color"){type=NavType.StringType})){
                navBackStack ->  val id: Int = navBackStack.arguments?.getInt("id") ?: 1
            val name: String = navBackStack.arguments?.getString("name") ?: " "
            val color: String = navBackStack.arguments?.getString("color") ?: "Black"
            ScalesScreen(navController = navController, id, name, color)
        }
        composable("counter_screen/{id}/{name}/{color}", arguments = listOf(navArgument("id") {type = NavType.IntType},
            navArgument("name") {type = NavType.StringType}, navArgument("color"){type=NavType.StringType})){
                navBackStack ->  val id: Int = navBackStack.arguments?.getInt("id") ?: 1
            val name: String = navBackStack.arguments?.getString("name") ?: " "
            val color: String = navBackStack.arguments?.getString("color") ?: "Black"
            CounterScreen(navController = navController, id, name, color)
        }
        composable("check_list_screen/{id}/{name}/{color}", arguments = listOf(navArgument("id") {type = NavType.IntType},
            navArgument("name") {type = NavType.StringType}, navArgument("color"){type=NavType.StringType})){
                navBackStack ->  val id: Int = navBackStack.arguments?.getInt("id") ?: 1
            val name: String = navBackStack.arguments?.getString("name") ?: " "
            val color: String = navBackStack.arguments?.getString("color") ?: "Black"
            CheckListScreen(navController = navController, id, name, color)
        }
    }
}