package com.huydiem.cleaningroommanager.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.R
import com.huydiem.cleaningroommanager.model.LoginScreenViewModel
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import com.huydiem.cleaningroommanager.ui.components.GoogleSignInButton
import com.huydiem.cleaningroommanager.utils.LoadingState
import kotlinx.coroutines.tasks.await

@ExperimentalMaterialApi
@Composable
fun LoginScreen(viewModel: LoginScreenViewModel) {

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.loadingState.collectAsState()

    // Equivalent of onActivityResult
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithCredential(credential)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = {
                        Text(text = "Login")
                    },
                    navigationIcon = {
                        IconButton(onClick = { Router.navigateTo(Screen.Home) }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            Firebase.auth.signOut()
                            Router.navigateTo(Screen.Home)
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.ExitToApp,
                                contentDescription = null,
                            )
                        }
                    }
                )
                if (state.status == LoadingState.Status.RUNNING) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = userEmail,
                        label = {
                            Text(text = "Email")
                        },
                        onValueChange = {
                            userEmail = it
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        value = userPassword,
                        label = {
                            Text(text = "Password")
                        },
                        onValueChange = {
                            userPassword = it
                        }
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                        content = {
                            Text(text = "Login")
                        },
                        onClick = {
                            viewModel.signInWithEmailAndPassword(
                                userEmail.trim(),
                                userPassword.trim()
                            )
                        }
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        text = "Or"
                    )

                    val context = LocalContext.current
                    val token =
                        "1098667494586-gimon39pvl92hd8785jra54j0s08evct.apps.googleusercontent.com"

                    GoogleSignInButton(
                        text = "Sign In With Google",
                        loadingText = "Signing In. . . .",
                        onClicked = {
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(token)
                                    .requestEmail()
                                    .build()

                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    )

                    OutlinedButton(
                        border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {

                        },
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.onPrimary),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    Icon(
                                        tint = Color.Transparent,
                                        imageVector = Icons.Default.AccountBox,
                                        contentDescription = null,
                                    )

                                    Text(
                                        style = MaterialTheme.typography.button,
                                        color = MaterialTheme.colors.onSurface,
                                        textAlign = TextAlign.Center,
                                        text = "Register"
                                    )
                                    Icon(
                                        tint = Color.Transparent,
                                        imageVector = Icons.Default.AccountBox,
                                        contentDescription = null,
                                    )
                                }
                            )
                        }
                    )

                    when (state.status) {
                        LoadingState.Status.SUCCESS -> {
                            Text(text = "Success")
                            Router.navigateTo(Screen.Home)
                        }
                        LoadingState.Status.FAILED -> {
                            Text(text = state.msg ?: "Error")
                        }
                        else -> {}
                    }
                }
            )
        }
    )
}