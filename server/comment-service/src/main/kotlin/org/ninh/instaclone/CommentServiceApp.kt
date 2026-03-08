package org.ninh.instaclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class CommentServiceApp
fun main(args: Array<String>) {
    runApplication<CommentServiceApp>(*args)
}