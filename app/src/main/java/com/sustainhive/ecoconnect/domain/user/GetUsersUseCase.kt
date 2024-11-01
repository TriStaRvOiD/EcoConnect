package com.sustainhive.ecoconnect.domain.user

import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.data.user.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(listOfUsers: List<String>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit): List<User> {
        return userRepository.getUsers(
            listOfUserIds = listOfUsers
        )
    }
}