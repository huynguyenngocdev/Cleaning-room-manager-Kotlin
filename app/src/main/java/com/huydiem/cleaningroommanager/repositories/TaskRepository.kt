package com.huydiem.cleaningroommanager.repositories

import com.google.firebase.firestore.CollectionReference
import com.huydiem.cleaningroommanager.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository
@Inject
constructor(
    private val taskList: CollectionReference
) {

    fun addNewTask(task: TaskModel) {
        try {
            taskList.document(task.id).set(task)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTaskList(): Flow<Result<List<TaskModel>>> = flow {
        try {
            emit(Result.Loading<List<TaskModel>>())

            val taskList = taskList.get().await().map { document ->
                document.toObject(TaskModel::class.java)
            }

            emit(Result.Success<List<TaskModel>>(data = taskList))

        } catch (e: Exception) {
            emit(Result.Error<List<TaskModel>>(message = e.localizedMessage ?: "Error Desconocido"))
        }
    }

    fun getTaskById(taskId: String): Flow<Result<TaskModel>> = flow {
        try {
            emit(Result.Loading<TaskModel>())

            val task = taskList
                .whereGreaterThanOrEqualTo("id", taskId)
                .get()
                .await()
                .toObjects(TaskModel::class.java)
                .first()


            emit(Result.Success<TaskModel>(data = task))

        } catch (e: Exception) {
            emit(Result.Error<TaskModel>(message = e.localizedMessage ?: "Error Desconocido"))
        }
    }

    fun updateTask(taskId: String, task: TaskModel) {
        try {
            val map = mapOf(
                "task" to task.task,
                "description" to task.description,
                "status" to task.status
            )

            taskList.document(taskId).update(map)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteTask(taskId: String) {
        try {
            taskList.document(taskId).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}