package org.ninh.instaclone

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform