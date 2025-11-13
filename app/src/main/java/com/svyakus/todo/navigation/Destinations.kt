package com.svyakus.todo.navigation

import kotlinx.serialization.Serializable

@Serializable
data object TodoList

@Serializable
data class Edit(val uid: String)

@Serializable
data object Create