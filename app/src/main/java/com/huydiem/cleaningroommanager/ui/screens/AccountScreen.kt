package com.huydiem.cleaningroommanager.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.huydiem.cleaningroommanager.CleaningRoomManager.Companion.currentUser
import com.huydiem.cleaningroommanager.R
import com.huydiem.cleaningroommanager.model.LoginScreenViewModel
import com.huydiem.cleaningroommanager.model.UserModel
import com.huydiem.cleaningroommanager.routing.Router
import com.huydiem.cleaningroommanager.routing.Screen
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun AccountScreen(user: UserModel, viewModel: LoginScreenViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            Surface(shape = CircleShape,
                modifier = Modifier.padding(bottom = 10.dp),
                elevation = 4.dp,
                content = {
                    Image(
                        painter = rememberImagePainter(
                            data = user.photoUrl,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.loading_icon)
                                fallback(R.drawable.loading_icon)
                                error(R.drawable.loading_icon)
                            }),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(150.dp)
                            .width(150.dp)
                    )
                }
            )

            Divider(color = Color.Cyan, thickness = 1.dp)
            Text(
                modifier = Modifier.padding(8.dp),
                text = "${user.name}",
                softWrap = true,
                style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 18.sp)
            )
            Divider(color = Color.Cyan, thickness = 1.dp)
            Text(
                modifier = Modifier.padding(8.dp),
                text = "${user.email}",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                softWrap = true,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp
                )
            )
            Divider(color = Color.Cyan, thickness = 1.dp)
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    coroutineScope.launch { viewModel.resetCurrentUser() }
                    currentUser = null
                    context.cacheDir.deleteRecursively()
                    Router.navigateTo(Screen.NeedLogin)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 70.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(text = "Sign Out")
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
            }
        } else {
            Text(text = "Bạn chưa đăng nhập")
        }
    }
}