package com.example.schooldiary20.di

import com.example.schooldiary20.repository.AuthRepository
import com.example.schooldiary20.repository.AuthRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImp
    ): AuthRepository
}