package com.huydiem.cleaningroommanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.model.LoginScreenViewModel
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import com.huydiem.cleaningroommanager.ui.screens.LoginScreen
import com.huydiem.cleaningroommanager.ui.screens.homeScreen.HomeScreen
import com.huydiem.cleaningroommanager.ui.theme.CleaningRoomManagerTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val user = MutableLiveData(Firebase.auth.currentUser)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleaningRoomManagerTheme {
                val state = user.observeAsState()
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    when (Router.currentScreen) {
                        is Screen.Home -> HomeScreen(user = user)
                        is Screen.Login -> LoginScreen(viewModel = LoginScreenViewModel())
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()

        // Save whatever you want in your live data, this is just an example
        user.value = Firebase.auth.currentUser
    }
}

//@Composable
//fun ShowScreen(){
//    when(Router.currentScreen){
//is Screen.Member ->
//    }
//}