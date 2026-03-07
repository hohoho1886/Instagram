package org.ninh.instaclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class FanoutConsumerApp
fun main(args: Array<String>) {
    runApplication<FanoutConsumerApp>(*args)
}