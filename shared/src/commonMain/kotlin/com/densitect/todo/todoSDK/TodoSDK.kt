package com.densitect.todo.todoSDK

import com.densitect.todo.cache.Database
import com.densitect.todo.cache.DatabaseDriverFactory
import com.densitect.todo.entity.TodoDTO
import com.densitect.todo.network.TodoAPI

class TodoSDK(databaseDriverFactory: DatabaseDriverFactory, val api: TodoAPI) {
    private val database = Database(databaseDriverFactory)

    suspend fun getAllTodos(forceReload: Boolean): List<TodoDTO> {
        val cachedTodos = database.getAllTodo()
        return if (cachedTodos.isEmpty() || forceReload) {
            api.getAllTodos().also {
                database.clearAndInsertAllTodo(it)
            }
        } else {
            cachedTodos
        }
    }
}