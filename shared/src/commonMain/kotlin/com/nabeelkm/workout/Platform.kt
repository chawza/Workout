package com.nabeelkm.workout

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform