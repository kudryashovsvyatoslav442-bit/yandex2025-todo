package com.svyakus.todo.di

import android.content.Context
import com.svyakus.todo.data.FileStorage
import com.svyakus.todo.data.TodoRepository
import com.svyakus.todo.data.network.TodoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    // ВСТАВЬ СВЕЖИЙ ТОКЕН СЮДА ВМЕСТО <TOKEN>
                    .header("Authorization", "Bearer y0__xCs_dC2BhjRhREgmLHE2hUw3cXusQijXHbkGXB6nKq2ceUGLeYK5s7-kw")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            // Игнорируем ошибку сертификата (beta vs hive)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoApi(client: OkHttpClient): TodoApi {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todo/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTodosRepository(
        storage: FileStorage,
        api: TodoApi
    ): TodoRepository = TodoRepository(storage = storage, api = api)
}