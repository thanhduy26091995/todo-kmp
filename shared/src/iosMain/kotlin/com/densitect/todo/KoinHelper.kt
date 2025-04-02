package com.densitect.todo

import com.densitect.todo.cache.IOSDatabaseDriverFactory
import com.densitect.todo.entity.TodoDTO
import com.densitect.todo.network.TodoAPI
import com.densitect.todo.todoSDK.TodoSDK
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinHelper : KoinComponent {
    private val sdk: TodoSDK by inject<TodoSDK>()

    suspend fun getTodos(forceReload: Boolean): List<TodoDTO> {
        return sdk.getAllTodos(forceReload)
    }
}

fun initKoin() {
    startKoin {
        modules(module {
            single<TodoAPI> {
                TodoAPI()
            }

            single {
                TodoSDK(databaseDriverFactory = IOSDatabaseDriverFactory(), api = get())
            }
        })
    }
}