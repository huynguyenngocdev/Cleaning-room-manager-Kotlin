package com.huydiem.cleaningroommanager.model


data class RoomModel(
    val id: String,
    val roomName: String,
    val member: List<UserModel>,
    val task: List<TaskModel>,
    val assignedTo: UserModel
){

    constructor() : this("", "", listOf(), listOf(), UserModel("","",""))
}