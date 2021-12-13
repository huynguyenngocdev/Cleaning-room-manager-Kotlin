package com.huydiem.cleaningroommanager.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

sealed class Destination(
    val route: String,
    val arguments: List<NamedNavArgument>
) {
    object TaskList : Destination("taskList", emptyList())
    object TaskDetail : Destination(
        route = "taskDetail",
        arguments = listOf(
            navArgument("taskId") {
                nullable = true
            }
        )
    )
}