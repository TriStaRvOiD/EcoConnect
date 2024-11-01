package com.sustainhive.ecoconnect.di.user

import com.google.firebase.firestore.FirebaseFirestore
import com.sustainhive.ecoconnect.data.event.repository.EventRepository
import com.sustainhive.ecoconnect.data.user.repository.UserRepository
import com.sustainhive.ecoconnect.data.user.repository.UserRepositoryImpl
import com.sustainhive.ecoconnect.domain.user.DeleteUserUseCase
import com.sustainhive.ecoconnect.domain.user.GetUserNameUseCase
import com.sustainhive.ecoconnect.domain.user.GetUserUseCase
import com.sustainhive.ecoconnect.domain.user.GetUsersUseCase
import com.sustainhive.ecoconnect.domain.user.UpsertUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesGetUserNameUseCase(userRepository: UserRepository): GetUserNameUseCase {
        return GetUserNameUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun providesGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun providesGetUsersUseCase(userRepository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpsertUseCase(userRepository: UserRepository): UpsertUserUseCase {
        return UpsertUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(userRepository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }
}