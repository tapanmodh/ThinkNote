package com.tm.thinknote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform