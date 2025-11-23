package me.sasta.spendings_processor_cmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform