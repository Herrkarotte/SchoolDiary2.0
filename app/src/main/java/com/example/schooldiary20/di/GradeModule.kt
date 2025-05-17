package com.example.schooldiary20.di

import com.example.schooldiary20.repository.GradeRepository
import com.example.schooldiary20.repository.GradeRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GradeModule {
    @Binds
    abstract fun bindGradeRepository(
        gradeRepositoryImp: GradeRepositoryImp
    ): GradeRepository
}