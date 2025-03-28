package com.densitect.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    val viewModel = koinViewModel<TodoListViewModel>()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val state by remember {
        viewModel.state
    }

    val pullToRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.loadTodos(forceReload = true)
    })

    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    "Todo list",
                )
            })
        }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullToRefreshState)
            ) {
                LazyColumn {
                    items(state.todos) { todo ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(todo.title)
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullToRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}