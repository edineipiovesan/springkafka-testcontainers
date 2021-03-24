package com.edineipiovesan.springkafkatestcontainers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class KafkaListener {

    @KafkaListener(topics = ["my-topic"])
    fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val value = data.value()
        print(value)

        acknowledgment.acknowledge()
    }
}