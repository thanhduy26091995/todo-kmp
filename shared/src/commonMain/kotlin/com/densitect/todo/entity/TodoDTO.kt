package com.densitect.todo.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("completed")
    val isDone: Boolean,
)