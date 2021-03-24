package com.edineipiovesan.springkafkatestcontainers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringkafkaTestcontainersApplication

fun main(args: Array<String>) {
    runApplication<SpringkafkaTestcontainersApplication>(*args)
}
