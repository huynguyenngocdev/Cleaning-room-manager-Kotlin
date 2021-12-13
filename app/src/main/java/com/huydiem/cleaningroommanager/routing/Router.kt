package com.huydiem.cleaningroommanager.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Class defining all possible screens in the app.
 */
sealed class Screen {
    object Home : Screen()
    object NeedLogin : Screen()
    object Login : Screen()
    object TaskDetail: Screen()
    object Account : Screen()
    object Member : Screen()
}

/**
 * Allows you to change the screen in the [MainActivity]
 *
 * Also keeps track of the current screen.
 */
object Router {
    var currentScreen: Screen by mutableStateOf(Screen.NeedLogin)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}
