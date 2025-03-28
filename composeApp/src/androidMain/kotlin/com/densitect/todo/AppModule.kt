package com.densitect.todo

import com.densitect.todo.cache.AndroidDatabaseDriverFactory
import com.densitect.todo.network.TodoAPI
import com.densitect.todo.todoSDK.TodoSDK
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<TodoAPI> { TodoAPI() }
    single<TodoSDK> {
        TodoSDK(
            databaseDriverFactory = AndroidDatabaseDriverFactory(androidContext()),
            api = get()
        )
    }

    viewModel {
        TodoListViewModel(todoSDK = get())
    }
}