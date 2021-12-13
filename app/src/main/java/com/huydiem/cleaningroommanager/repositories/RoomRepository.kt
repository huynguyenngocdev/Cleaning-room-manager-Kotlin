package com.huydiem.cleaningroommanager.repositories

import com.google.firebase.firestore.CollectionReference
import com.huydiem.cleaningroommanager.model.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RoomRepository
@Inject
constructor(
    private val roomList: CollectionReference
) {

    fun addNewRoom(room: RoomModel) {
        try {
            roomList.document(room.id).set(room)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRoomList(): Flow<Result<List<RoomModel>>> = flow {
        try {
            emit(Result.Loading<List<RoomModel>>())

            val roomList = roomList.get().await().map { document ->
                document.toObject(RoomModel::class.java)
            }

            emit(Result.Success<List<RoomModel>>(data = roomList))

        } catch (e: Exception) {
            emit(Result.Error<List<RoomModel>>(message = e.localizedMessage ?: "Error Desconocido"))
        }
    }

    fun getRoomById(roomId: String): Flow<Result<RoomModel>> = flow {
        try {
            emit(Result.Loading<RoomModel>())

            val room = roomList
                .whereGreaterThanOrEqualTo("id", roomId)
                .get()
                .await()
                .toObjects(RoomModel::class.java)
                .first()


            emit(Result.Success<RoomModel>(data = room))

        } catch (e: Exception) {
            emit(Result.Error<RoomModel>(message = e.localizedMessage ?: "Error Desconocido"))
        }
    }

    fun updateRoom(roomId: String, room: RoomModel) {
        try {
            val map = mapOf(
                "roomName" to room.roomName,
                "member" to room.member,
                "task" to room.task,
                "assignTo" to room.assignedTo,
            )

            roomList.document(roomId).update(map)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteRoom(roomId: String) {
        try {
            roomList.document(roomId).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}