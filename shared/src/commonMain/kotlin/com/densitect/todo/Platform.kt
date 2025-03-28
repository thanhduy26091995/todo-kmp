package com.densitect.todo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform