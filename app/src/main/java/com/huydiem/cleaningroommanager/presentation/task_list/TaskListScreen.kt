package com.huydiem.cleaningroommanager.presentation.task_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.currentUser
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.errorTaskList
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.isLoadingTaskList
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.stateTaskDetail
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.stateTaskList
import com.huydiem.cleaningroommanager.model.TaskModel
import com.huydiem.cleaningroommanager.presentation.task_list.components.TaskListItem
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import kotlinx.coroutines.delay

@Composable
fun getTaskData(): MutableState<List<TaskModel>> {
    var tasks: MutableState<List<TaskModel>> = remember {
        mutableStateOf(emptyList())
    }
    Firebase.firestore.collection("tasks").get().addOnSuccessListener { result ->
        tasks.value = result.toObjects(TaskModel::class.java)
        for ((index, e) in result.withIndex()) {
            tasks.value[index].id = e.id
            //            TaskModel(
//                id = e.id,
//                task = e.data["task"].toString(),
//                description = e.data["description"].toString(),
//                status = e.data["status"].toString() == "true",
//                userId = e.data["userId"].toString()
//            )
        }
    }

//    val tasks: List<TaskModel> = tempResult.result.toObjects(TaskModel::class.java)

//verifier task of user
    val taskList: MutableList<TaskModel> = mutableListOf()
    val taskNeedDelete: MutableList<TaskModel> = mutableListOf()
    taskList.addAll(tasks.value)
    taskList.map { element ->
        if (element.userId != currentUser?.uid ?: null) {
            taskNeedDelete.add(element)
        }
    }
    if (taskNeedDelete.isNotEmpty()) {
        taskNeedDelete.map { element ->
            taskList.remove(element)
        }
    }

    tasks.value = taskList
//    tasks.value.onEach { result ->
//        Log.d("Success data", "${result.task}")
//    }
    return tasks
}

fun deleteTask(id: String) {
    Firebase.firestore.collection("tasks").document(id).delete().addOnSuccessListener {
        Log.d(
            "Delete Action",
            "DocumentSnapshot successfully deleted!"
        )
    }.addOnFailureListener { e -> Log.w("Delete Action", "Error deleting document", e) }
}

@ExperimentalMaterialApi
@Composable
fun TaskListScreen(
) {
//    val tasks: MutableState<List<TaskModel>> = remember {
//        mutableStateOf(getTaskData())
//    }

    var tasks = getTaskData()

    stateTaskList = tasks

    var isRefreshing by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
//                tasks = getTaskData()
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = tasks.value,
                    itemContent = { task ->
                        var isDeleted by remember { mutableStateOf(false) }
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd) isDeleted = !isDeleted
                                it != DismissValue.DismissedToEnd
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.StartToEnd),
                            dismissThresholds = {
                                FractionalThreshold(0.5f)
                            },
                            background = {
                                val direction =
                                    dismissState.dismissDirection ?: return@SwipeToDismiss
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.LightGray
                                        DismissValue.DismissedToEnd -> Color.Red
                                        DismissValue.DismissedToStart -> Color.Red
                                    }
                                )
                                val alignment = when (direction) {
                                    DismissDirection.StartToEnd -> Alignment.CenterStart
                                    DismissDirection.EndToStart -> Alignment.CenterEnd
                                }
                                val icon = when (direction) {
                                    DismissDirection.StartToEnd -> Icons.Default.Delete
                                    DismissDirection.EndToStart -> Icons.Default.Delete
                                }
                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Localized description",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            }
                        ) {
                            if (isDeleted) {
                                deleteTask(task.id)
                            } else {
                                TaskListItem(
                                    task,
                                    onItemClick = {
                                        Router.navigateTo(Screen.TaskDetail)
                                        stateTaskDetail = task
                                    }
                                )
                            }
                        }

                    }

                )
            }
        }
    }
}