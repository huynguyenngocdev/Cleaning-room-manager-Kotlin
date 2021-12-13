package com.huydiem.cleaningroommanager.model

import android.net.Uri
import java.util.*

data class TaskModel(
    var id: String,
    val task: String,
    val description: String,
    val status: Boolean,
    val userId: String
) {
    constructor() : this("", "", "", false, "")
}