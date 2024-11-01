package com.sustainhive.ecoconnect.domain.user

import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.data.user.repository.UserRepository

class UpsertUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        return userRepository.upsertUser(
            user,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}