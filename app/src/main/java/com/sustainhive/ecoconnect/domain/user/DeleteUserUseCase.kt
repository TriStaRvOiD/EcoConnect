package com.sustainhive.ecoconnect.domain.user

import com.sustainhive.ecoconnect.data.user.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userRepository.deleteUser(
            userId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}