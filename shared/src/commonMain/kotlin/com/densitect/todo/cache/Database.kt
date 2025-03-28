package com.densitect.todo.cache

import com.densitect.todo.entity.TodoDTO

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = TodoDatabase(databaseDriverFactory.createDriver())
    private val queries = database.todoDatabaseQueries

    internal fun getAllTodo(): List<TodoDTO> {
        return queries.selectAllTodo().executeAsList().map {
            with(it) {
                TodoDTO(id, title, isDone == 1L)
            }
        }
    }

    internal fun insertTodo(title: String, isDone: Boolean) {
        queries.insertTodo(title, isDone = if (isDone) 1 else 0)
    }

    internal fun removeAllTodo() {
        queries.removeAllTodo()
    }

    internal fun clearAndInsertAllTodo(todos: List<TodoDTO>) {
        queries.transaction {
            removeAllTodo()
            todos.forEach {
                insertTodo(it.title, it.isDone)
            }
        }
    }
}