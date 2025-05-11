package com.example.schooldiary20.di

import com.example.schooldiary20.repository.ScheduleRepository
import com.example.schooldiary20.repository.ScheduleRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleModule {
    @Binds
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImp
    ): ScheduleRepository
}