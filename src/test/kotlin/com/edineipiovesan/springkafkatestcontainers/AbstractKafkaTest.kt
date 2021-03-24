package com.edineipiovesan.springkafkatestcontainers

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.*

@Testcontainers
abstract class AbstractKafkaTest {

    @Autowired
    lateinit var producer: KafkaTemplate<String, Any>

    protected fun sendMessage(topic: String, key: String = UUID.randomUUID().toString(), message: String) {
        val record = ProducerRecord<String, Any>(topic, key, message)
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
        fun producerConfigs(): Map<String, Any> {
            val props: MutableMap<String, Any> = HashMap()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            return props
        }

        @Bean
        fun producerFactory(): ProducerFactory<String, Any> = DefaultKafkaProducerFactory(producerConfigs())

        @Bean
        fun kafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(producerFactory())

        fun consumerProps(): Map<String, Any> {
            val props = HashMap<String, Any>();
            props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
            props[ConsumerConfig.GROUP_ID_CONFIG] = UUID.randomUUID().toString()
            props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class
            props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class
            return props;
        }

        @Bean
        fun consumerFactory(): ConsumerFactory<String, Any> = DefaultKafkaConsumerFactory(consumerProps());

        @Bean
        fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
            val factory = ConcurrentKafkaListenerContainerFactory<String, Any>();
            factory.consumerFactory = consumerFactory();
            return factory;
        }
    }
}
