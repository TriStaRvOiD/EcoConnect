package com.sustainhive.ecoconnect.domain.user

import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.data.user.repository.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit): User? {
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return userRepository.getUser(
            userId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}