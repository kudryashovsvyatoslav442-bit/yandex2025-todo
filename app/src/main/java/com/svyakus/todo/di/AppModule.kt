package com.svyakus.todo.di

import android.content.Context
import com.svyakus.todo.data.FileStorage
import com.svyakus.todo.data.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFileStorage(
        @ApplicationContext context: Context
    ): FileStorage = FileStorage(context = context)

    @Provides
    @Singleton
    fun provideTodosRepository(
        storage: FileStorage
    ): TodoRepository = TodoRepository(storage = storage)

}