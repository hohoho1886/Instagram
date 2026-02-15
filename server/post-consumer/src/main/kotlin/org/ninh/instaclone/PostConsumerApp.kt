package org.ninh.instaclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PostConsumerApp

fun main(args: Array<String>) {
    runApplication<PostConsumerApp>(*args)
}