package com.sustainhive.ecoconnect.di.event

import com.google.firebase.firestore.FirebaseFirestore
import com.sustainhive.ecoconnect.data.event.repository.EventRepository
import com.sustainhive.ecoconnect.data.event.repository.EventRepositoryImpl
import com.sustainhive.ecoconnect.domain.event.DeleteEventUseCase
import com.sustainhive.ecoconnect.domain.event.GetEventsCreatedByUserUseCase
import com.sustainhive.ecoconnect.domain.event.GetEventsJoinedByUserUseCase
import com.sustainhive.ecoconnect.domain.event.GetLatestEventsUseCase
import com.sustainhive.ecoconnect.domain.event.GetSpecificEventUseCase
import com.sustainhive.ecoconnect.domain.event.GetTrendingEventsUseCase
import com.sustainhive.ecoconnect.domain.event.JoinEventUseCase
import com.sustainhive.ecoconnect.domain.event.LeaveEventUseCase
import com.sustainhive.ecoconnect.domain.event.UpsertEventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideEventRepository(firestore: FirebaseFirestore): EventRepository {
        return EventRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideGetSpecificEventUseCase(eventRepository: EventRepository): GetSpecificEventUseCase {
        return GetSpecificEventUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideGetLatestEventsUseCase(eventRepository: EventRepository): GetLatestEventsUseCase {
        return GetLatestEventsUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideGetTrendingEventsUseCase(eventRepository: EventRepository): GetTrendingEventsUseCase {
        return GetTrendingEventsUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideGetEventsCreatedByUserUseCase(eventRepository: EventRepository): GetEventsCreatedByUserUseCase {
        return GetEventsCreatedByUserUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideGetEventsJoinedByUserUseCase(eventRepository: EventRepository): GetEventsJoinedByUserUseCase {
        return GetEventsJoinedByUserUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideUpsertEventUseCase(eventRepository: EventRepository): UpsertEventUseCase {
        return UpsertEventUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteEventUseCase(eventRepository: EventRepository): DeleteEventUseCase {
        return DeleteEventUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideJoinEventUseCase(eventRepository: EventRepository): JoinEventUseCase {
        return JoinEventUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideLeaveEventUseCase(eventRepository: EventRepository): LeaveEventUseCase {
        return LeaveEventUseCase(eventRepository)
    }
}