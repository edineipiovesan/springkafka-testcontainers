package com.edineipiovesan.springkafkatestcontainers.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class FourthKafkaListener {
    private final val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["my-topic4"])
    fun onMessage(data: ConsumerRecord<String, Any>) =
        logger.info(
            "Event received; " +
                    "value=${data.value()}; " +
                    "topic=${data.topic()}; " +
                    "partition=${data.partition()}; " +
                    "offset=${data.offset()}"
        )
}