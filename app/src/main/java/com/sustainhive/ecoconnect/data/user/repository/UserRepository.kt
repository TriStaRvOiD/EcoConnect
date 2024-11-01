package com.sustainhive.ecoconnect.data.user.repository

import com.sustainhive.ecoconnect.data.user.model.User

interface UserRepository {
    //Retrieval
    suspend fun getUser(userId: String, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit): User?
    suspend fun getUsers(listOfUserIds: List<String>): List<User>
    suspend fun getUserName(userId: String, onFailure: (e: Exception) -> Unit): String

    //Modification
    suspend fun upsertUser(user: User, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit)
    suspend fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit)//Also delete created and joined events
}