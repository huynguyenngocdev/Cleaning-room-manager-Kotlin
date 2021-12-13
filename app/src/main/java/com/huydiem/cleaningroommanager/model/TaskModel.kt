package com.huydiem.cleaningroommanager.model

data class TaskModel(
    val id: String,
    val task: String,
    val status: Boolean,
){
    constructor() : this("", "",false)
}