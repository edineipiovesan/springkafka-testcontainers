package com.edineipiovesan.springkafkatestcontainers.listener

import com.edineipiovesan.springkafkatestcontainers.configuration.AbstractKafkaTest
import com.edineipiovesan.springkafkatestcontainers.configuration.KafkaTestcontainersConfiguration
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [KafkaAutoConfiguration::class, ThirdKafkaListener::class])
@Import(KafkaTestcontainersConfiguration::class)
class ThirdKafkaListenerTest : AbstractKafkaTest("my-topic3") {

    @SpykBean
    lateinit var listener: ThirdKafkaListener

    @Test
    fun `message successfully received`() {
        val message = "This is a message sent by ${this.javaClass.simpleName}"
        sendMessage(message = message)

        verify(exactly = 1, timeout = 60000) {
            listener.onMessage(match { it.value() == message })
        }
    }

    @Test
    fun `another message successfully received`() {
        val message = "This is another message sent by ${this.javaClass.simpleName}"
        sendMessage(message = message)

        verify(exactly = 1, timeout = 60000) {
            listener.onMessage(match { it.value() == message })
        }
    }

    @Test
    fun `one more message successfully received`() {
        val message = "This is one more message sent by ${this.javaClass.simpleName}"
        sendMessage(message = message)

        verify(exactly = 1, timeout = 60000) {
            listener.onMessage(match { it.value() == message })
        }
    }
}
