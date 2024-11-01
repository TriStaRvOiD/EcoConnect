package com.sustainhive.ecoconnect.data.user.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sustainhive.ecoconnect.data.user.model.User
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(firestore: FirebaseFirestore) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getUserName(userId: String, onFailure: (e: Exception) -> Unit): String {
        return try {
            val document = usersCollection.document(userId)
                .get()
                .addOnFailureListener { e ->
                    onFailure(e)
                }
                .await()
            document.getString("name") ?: ""
        } catch (e: Exception) {
            onFailure(e)
            ""
        }
    }

    override suspend fun getUser(userId: String, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit): User? {
        return try {
            val snapshot = usersCollection.document(userId).get()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it)
                }
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            onFailure(e)
            null
        }
    }

    override suspend fun getUsers(listOfUserIds: List<String>): List<User> {
        return try {
            val snapshots = usersCollection.whereIn("id", listOfUserIds).get()
                .await()
            snapshots.toObjects(User::class.java)
        } catch (e: Exception) {
            // Handle the exception
            emptyList()
        }
    }

    override suspend fun upsertUser(user: User, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit) {
        try {
            usersCollection.document(user.id).set(user)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it)
                }
                .await()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override suspend fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit) {
        try {
            val snapshot = usersCollection.document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                usersCollection.document(userId).delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
                    .await()
            } else {
                onFailure(Exception("User does not exist"))
            }
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}