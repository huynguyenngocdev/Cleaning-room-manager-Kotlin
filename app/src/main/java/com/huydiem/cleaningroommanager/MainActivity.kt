package com.huydiem.cleaningroommanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.huydiem.cleaningroommanager.model.LoginScreenViewModel
import com.huydiem.cleaningroommanager.presentation.task_detail.TaskDetailScreen
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import com.huydiem.cleaningroommanager.ui.screens.LoginScreen
import com.huydiem.cleaningroommanager.ui.screens.NeedLoginScreen
import com.huydiem.cleaningroommanager.ui.screens.HomeScreen
import com.huydiem.cleaningroommanager.ui.theme.CleaningRoomManagerTheme


class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleaningRoomManagerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    when (Router.currentScreen) {
                        is Screen.NeedLogin -> NeedLoginScreen()
                        is Screen.Home -> HomeScreen(viewModel = LoginScreenViewModel())
                        is Screen.Login -> LoginScreen(viewModel = LoginScreenViewModel())
                        is Screen.TaskDetail -> TaskDetailScreen()
                        else -> {
                            NeedLoginScreen()
                        }
                    }
                }
            }
        }
    }

}

//@Composable
//fun ShowScreen(){
//    when(Router.currentScreen){
//is Screen.Member ->
//    }
//}