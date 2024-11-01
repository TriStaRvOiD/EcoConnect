package com.sustainhive.ecoconnect.presentation.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.domain.user.UpsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val upsertUserUseCase: UpsertUserUseCase,
    private val recaptchaClient: RecaptchaClient
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var authenticated by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun modifyName(value: String) {
        name = value
    }

    fun modifyEmail(value: String) {
        email = value
    }

    fun modifyPassword(value: String) {
        password = value
    }

    fun signUpToFirebase(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        isLoading = true
        try {
            reCaptchaCheck(
                recaptchaAction = RecaptchaAction.SIGNUP,
                onSuccess = {
                    firebaseAuth.createUserWithEmailAndPassword(
                        email,
                        password
                    ).addOnSuccessListener {
                        viewModelScope.launch {
                            upsertUserUseCase.invoke(
                                user = User(
                                    id = firebaseAuth.currentUser!!.uid,
                                    name = name,
                                    email = email,
                                    joinDate = LocalDate.now().toString()
                                ),
                                onSuccess = {
                                    isLoading = false
                                    authenticated = true
                                    onSuccess()
                                },
                                onFailure = {
                                    isLoading = false
                                    onFailure(it)
                                }
                            )
                        }
                    }.addOnFailureListener { error ->
                        isLoading = false
                        onFailure(error)
                    }
                },
                onFailure = { e ->
                    isLoading = false
                    onFailure(Exception(e))
                }
            )
        } catch (e: Exception) {
            isLoading = false
            onFailure(e)
        }
    }

    fun signInToFirebase(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        isLoading = true
        try {
            reCaptchaCheck(
                recaptchaAction = RecaptchaAction.LOGIN,
                onSuccess = { token ->
                    firebaseAuth.signInWithEmailAndPassword(
                        email,
                        password
                    ).addOnSuccessListener {
                        isLoading = false
                        authenticated = true
                        onSuccess(token)
                    }.addOnFailureListener { error ->
                        isLoading = false
                        onFailure(error)
                    }
                },
                onFailure = { t ->
                    isLoading = false
                    onFailure(Exception(t))
                }
            )
        } catch (e: Exception) {
            isLoading = false
            onFailure(e)
        }
    }

    fun resetPassword(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        isLoading = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener {
                isLoading = false
                onFailure(it)
            }
    }

    private fun reCaptchaCheck(
        recaptchaAction: RecaptchaAction,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            recaptchaClient
                .execute(recaptchaAction)
                .onSuccess { token ->
                    Log.d("hello token", token)
                    onSuccess(token)
                }
                .onFailure { exception ->
                    onFailure(exception)
                }
        }
    }
}