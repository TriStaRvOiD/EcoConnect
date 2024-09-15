package com.sustainhive.ecoconnect.presentation.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signUpToFirebase(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { error ->
            onFailure(error)
        }
    }

    fun signInToFirebase(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { error ->
            onFailure(error)
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}