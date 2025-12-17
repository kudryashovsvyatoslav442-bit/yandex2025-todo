package com.svyakus.todo.data.network

import retrofit2.Response
import retrofit2.http.*

interface TodoApi {

    @GET("list")
    suspend fun getList(): Response<TodoListResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body listRequest: TodoListResponse // Или отдельный Request объект, но структура та же
    ): Response<TodoListResponse>

    @GET("list/{id}")
    suspend fun getItem(@Path("id") id: String): Response<TodoItemResponse>

    @POST("list")
    suspend fun addItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: TodoItemResponse // Сервер ждет {"element": {...}}
    ): Response<TodoItemResponse>

    @PUT("list/{id}")
    suspend fun updateItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: TodoItemResponse
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoItemResponse>
}