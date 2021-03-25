package com.edineipiovesan.springkafkatestcontainers.configuration

import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.BeforeAll
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.*


@Testcontainers
abstract class AbstractKafkaTest(private val topicName: String) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var producer: KafkaTemplate<String, Any>

    protected fun sendMessage(topic: String = topicName, key: String = UUID.randomUUID().toString(), message: String) {
        val record = ProducerRecord<String, Any>(topic, key, message)
        val result = producer.send(record).get() //Send synchronous event
        logger.info(
            "Event sent; " +
                    "topic=${result.recordMetadata.topic()}; " +
                    "partition=${result.recordMetadata.partition()}; " +
                    "offset=${result.recordMetadata.offset()}; " +
                    "key=${result.producerRecord.key()}"
        )
    }

    companion object {
        val kafka: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
            .withReuse(true)
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger(this::class.java)).withSeparateOutputStreams())

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            if (!kafka.isRunning) kafka.start()
        }
    }
}