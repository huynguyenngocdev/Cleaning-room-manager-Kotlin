package com.huydiem.cleaningroommanager.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import com.huydiem.cleaningroommanager.ui.screens.AccountScreen

@ExperimentalMaterialApi
@Composable
fun HomeScreen(user: MutableLiveData<FirebaseUser?>) {
    val currentUser = remember {
        mutableStateOf(user)
    }
    val currentContent = remember { mutableStateOf("Room") }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUser.value != null) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Cleaning Room Manager", color = Color.White)
                            },

                            backgroundColor = Color(0xFF039CE4),
                            elevation = AppBarDefaults.TopAppBarElevation
                        )
                    },
                    content = {
//                        LazyColumn(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .padding(bottom = 150.dp)
//                        ) {
//                            items(items = foods, itemContent = { item ->
//                                Food(food = item)
//                            })
//                        }
                        when (currentContent.value) {
                            "Room" -> Text(text = "Room 212")
                            "Account" -> AccountScreen()
                            else -> {
                                Text(text = "conent")
                            }

                        }
                    },

                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        if (currentContent.value == "Room") {
                            FloatingActionButton(
                                onClick = {
                                    Router.navigateTo(Screen.Task)
                                },
                                contentColor = MaterialTheme.colors.background,
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Add a new task"
                                    )
                                }
                            )
                        }
                    },

                    bottomBar = {
                        BottomAppBar(content = {
                            val navController = rememberNavController()

                            BottomNavigation {
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "Room") },
                                    selected = currentContent.value == "Room",
                                    onClick = {
                                        currentContent.value = "Room"
                                    },
                                    alwaysShowLabel = false,
                                )

                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.List,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "Member") },
                                    selected = currentContent.value == "Member",
                                    onClick = {
                                        currentContent.value = "Member"
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

            } else {
                Text(
                    text = "You need login to use!",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h4,
                    fontSize = 30.sp
                )
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                    onClick = { Router.navigateTo(Screen.Login) }) {
                    Text(text = "Go to login ")
                }
            }

        }
    }
}