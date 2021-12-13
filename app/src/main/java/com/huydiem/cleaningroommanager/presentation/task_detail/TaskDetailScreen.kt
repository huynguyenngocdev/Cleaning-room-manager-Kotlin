package com.huydiem.cleaningroommanager.presentation.task_detail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.stateTaskDetail
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.isLoadingTaskDetail
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.errorTaskDetail
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.currentUser

fun addNewTask(task: String, description: String) {
    val data = hashMapOf(
        "task" to task,
        "description" to description,
        "status" to false,
        "userId" to (currentUser?.uid ?: null)
    )

    Firebase.firestore.collection("tasks").add(data).addOnSuccessListener { documentReference ->
        Log.d("Create Task Activity", "DocumentSnapshot written with ID: ${documentReference.id}")
    }.addOnFailureListener { e ->
        Log.w("Create Task Activity", "Error adding document", e)
    }

    Router.navigateTo(Screen.Home)
}

fun updateTask(id: String, task: String, description: String, status: Boolean) {
    val docRef = Firebase.firestore.collection("tasks").document(id)

    Firebase.firestore.runTransaction { transaction ->
        transaction.update(docRef, "task", task)
        transaction.update(docRef, "description", description)
        transaction.update(docRef, "status", status)
    }

    Router.navigateTo(Screen.Home)
}

@Composable
fun TaskDetailScreen() {
    var id by remember(stateTaskDetail?.id) { mutableStateOf(stateTaskDetail?.id ?: "") }
    var task by remember(stateTaskDetail?.task) { mutableStateOf(stateTaskDetail?.task ?: "") }
    var description by remember(stateTaskDetail?.description) {
        mutableStateOf(
            stateTaskDetail?.description ?: ""
        )
    }
    var status by remember(stateTaskDetail?.status) {
        mutableStateOf(
            stateTaskDetail?.status ?: false
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 1.dp,
                title = {
                    Text(text = "Back to Task List")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Router.navigateTo(Screen.Home)
                        stateTaskDetail = null
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        value = task,
                        onValueChange = { task = it },
                        label = {
                            Text(text = "Task")
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text(text = "Description")
                        }
                    )
                    if (stateTaskDetail != null) {
                        Button(
                            onClick = { status = !status },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            enabled = !status
                        ) {
                            Text(text = if (status) "Done" else "Mask as Done")
                        }
                    }
                }

                if (errorTaskDetail.isNotBlank()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        text = errorTaskDetail,
                        style = TextStyle(
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                if (isLoadingTaskDetail) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    if (stateTaskDetail?.id != null) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            onClick = {
                                updateTask(
                                    id = id,
                                    task = task,
                                    description = description,
                                    status = status
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Update Task",
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            onClick = {
                                Log.d("Create", " new task detail")
                                addNewTask(task, description)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Add New Task",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}