package com.sustainhive.ecoconnect.domain.user

import com.sustainhive.ecoconnect.data.user.repository.UserRepository

class GetUserNameUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String, onFailure: (Exception) -> Unit): String {
        return userRepository.getUserName(
            userId = userId,
            onFailure = onFailure
        )
    }
}