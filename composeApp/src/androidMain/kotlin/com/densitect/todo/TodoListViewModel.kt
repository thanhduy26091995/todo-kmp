package com.densitect.todo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.densitect.todo.entity.TodoDTO
import com.densitect.todo.todoSDK.TodoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(private val todoSDK: TodoSDK) : ViewModel() {
    private val _state = mutableStateOf(TodoScreenState())
    val state: State<TodoScreenState> = _state

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadTodos(forceReload = false)
    }

    fun loadTodos(forceReload: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val todos = todoSDK.getAllTodos(forceReload = forceReload)
            _state.value = _state.value.copy(isLoading = false, todos = todos)
            _isRefreshing.value = false
        }
    }
}

data class TodoScreenState(
    val isLoading: Boolean = false,
    val todos: List<TodoDTO> = emptyList(),
)