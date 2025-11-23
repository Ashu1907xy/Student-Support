package com.example.collagemajorproject.di

import com.example.collagemajorproject.Repo.TimetableRepo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideFirebaseDataBase(): FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
    @Provides
    fun provideRepo(firebaseDatabase: FirebaseDatabase) : TimetableRepo {
        return TimetableRepo(firebaseDatabase)
    }
}