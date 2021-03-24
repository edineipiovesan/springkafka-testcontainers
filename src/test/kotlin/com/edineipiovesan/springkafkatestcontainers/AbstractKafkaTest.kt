package com.edineipiovesan.springkafkatestcontainers

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.*

@Testcontainers
abstract class AbstractKafkaTest {

    @Autowired
    lateinit var producer: KafkaTemplate<String, String>

    protected fun sendMessage(topic: String, key: String = UUID.randomUUID().toString(), message: String) {
        val record = ProducerRecord(topic, key, message)
        producer.send(record)
    }

    companion object {
        @Container
        val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            if (!kafka.isRunning)
                kafka.start()
        }
    }

    @TestConfiguration
    class KafkaTestContainersConfiguration {
        @Bean
        fun producerConfigs(): Map<String, Any> {
            val props: MutableMap<String, Any> = HashMap()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            return props
        }

        @Bean
        fun producerFactory(): ProducerFactory<String?, String?> = DefaultKafkaProducerFactory(producerConfigs())

        @Bean
        fun kafkaTemplate(): KafkaTemplate<String?, String?> = KafkaTemplate(producerFactory())
    }
}