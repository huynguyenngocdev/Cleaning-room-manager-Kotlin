package com.huydiem.cleaningroommanager.model

import android.net.Uri

data class UserModel(
    val uid : String,
    val email: String,
    val name: String,
    val photoUrl: Uri
)