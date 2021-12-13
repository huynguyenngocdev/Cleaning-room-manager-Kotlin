package com.huydiem.cleaningroommanager

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.huydiem.cleaningroommanager.model.TaskModel
import com.huydiem.cleaningroommanager.model.UserModel

class CleaningRoomManager : Application() {
    companion object {
        var currentUser: UserModel? = null

        //        TaskList
        var isLoadingTaskList: Boolean = false
        var stateTaskList: MutableState<List<TaskModel>> = mutableStateOf(emptyList())
        var errorTaskList: String = ""

        //        TaskDetail
        var isLoadingTaskDetail: Boolean = false
        var stateTaskDetail: TaskModel? = null
        var errorTaskDetail: String = ""
    }
}