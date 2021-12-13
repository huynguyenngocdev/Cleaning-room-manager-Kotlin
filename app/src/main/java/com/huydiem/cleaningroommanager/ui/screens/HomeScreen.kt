package com.huydiem.cleaningroommanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.model.LoginScreenViewModel
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.currentUser
import com.huydiem.cleaningroommanager.model.TaskModel
import com.huydiem.cleaningroommanager.presentation.task_list.TaskListScreen
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen

fun refreshAllTaskStatus() {
    Firebase.firestore.collection("tasks").get().addOnSuccessListener { result ->
        for (document in result) {
            val docRef = Firebase.firestore.collection("tasks").document(document.id)
            Firebase.firestore.runTransaction { transaction ->
                transaction.update(docRef, "status", false)
            }
        }
    }
    Router.navigateTo(Screen.Home)
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen(viewModel: LoginScreenViewModel) {
    val user = currentUser
    val currentContent = remember { mutableStateOf("TaskList") }
    val openDialog = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user != null) {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Cleaning Room Manager", color = Color.White)
                            },

                            backgroundColor = Color(0xFF039CE4),
                            actions = {
                                IconButton(onClick = {
                                    openDialog.value = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Refresh,
                                        contentDescription = null,
                                    )
                                }

                                if (openDialog.value) {
                                    Dialog(onDismissRequest = { openDialog.value = false }) {
                                        Card(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                                Modifier
                                                    .background(Color.White)
                                                    .padding(8.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Are you sure you want to Reset All Tasks Status?",
                                                    textAlign = TextAlign.Center,
                                                    style = TextStyle(
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 30.dp,
                                                            horizontal = 5.dp
                                                        )
                                                )
                                                Button(
                                                    onClick = {
                                                        refreshAllTaskStatus()
                                                        Router.navigateTo(Screen.Home)
                                                        openDialog.value = false
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 5.dp,
                                                            horizontal = 15.dp
                                                        ),
                                                    colors = ButtonDefaults.buttonColors(
                                                        backgroundColor = Color.Red
                                                    ),
                                                    shape = RoundedCornerShape(10)

                                                ) {
                                                    Text(text = "Yes", color = Color.White)
                                                }

                                                Button(
                                                    onClick = { openDialog.value = false },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 5.dp,
                                                            horizontal = 15.dp
                                                        ),
                                                    colors = ButtonDefaults.buttonColors(
                                                        backgroundColor = Color.Cyan
                                                    ),
                                                    shape = RoundedCornerShape(10)

                                                ) {
                                                    Text(text = "No", color = Color.White)
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            elevation = AppBarDefaults.TopAppBarElevation
                        )
                    },
                    content = {
                        //Navigation in Main Screen
                        when (currentContent.value) {
                            "TaskList" -> {
                                TaskListScreen()
                            }
                            "Account" -> AccountScreen(user = user, viewModel = viewModel)
                            else -> {
                                Text(text = "Incomplete")
                            }

                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        if (currentContent.value == "TaskList") {
                            FloatingActionButton(
                                onClick = { Router.navigateTo(Screen.TaskDetail) },
                                backgroundColor = Color.Cyan,
                                contentColor = MaterialTheme.colors.background
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add a new task"
                                )
                            }
                        }
                    },
                    bottomBar = {
                        BottomAppBar(content = {
                            BottomNavigation {
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.List,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "TaskList") },
                                    selected = currentContent.value == "TaskList",
                                    onClick = {
                                        currentContent.value = "TaskList"
                                    },
                                    alwaysShowLabel = false,
                                )

                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.AccountBox,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "Account") },
                                    selected = currentContent.value == "Account",
                                    onClick = {
                                        currentContent.value = "Account"
                                    },
                                    alwaysShowLabel = false,
                                )
                            }
                        })
                    }
                )
            }

        }
    }
}

//fun NavGraphBuilder.addTaskDetail() {
//    composable(
//        route = Destination.TaskDetail.route + "?taskId={taskId}"
//    ) {
//        val viewModel: TaskDetailViewModel = hiltViewModel()
//        val state = viewModel.state.value
//
//        TaskDetailScreen(
//            state = state,
//            addNewTask = viewModel::addNewTask,
//            updateTask = viewModel::updateTask
//        )
//    }
//}