package com.kairon007.pestotasks.di

import com.google.firebase.database.DatabaseReference
import com.kairon007.pestotasks.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTasksRepository(firebaseDatabase: DatabaseReference): TasksRepository =
        TasksRepository(firebaseDatabase)
}
