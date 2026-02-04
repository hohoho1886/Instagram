package org.ninh.instaclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application{
    @Bean
    fun hello(): (String) -> String = { name ->
        "Hello $name"
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}