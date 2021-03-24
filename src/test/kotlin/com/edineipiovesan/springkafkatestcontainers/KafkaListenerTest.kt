package com.edineipiovesan.springkafkatestcontainers

import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import


@SpringBootTest(classes = [KafkaListener::class])
@Import(AbstractKafkaTest.KafkaTestContainersConfiguration::class)
class KafkaListenerTest: AbstractKafkaTest() {

    @SpykBean
    lateinit var listener: KafkaListener

    @Test
    fun test() {
        sendMessage(topic = "my-topic", message = "this is a message sent by test()")

        verify(exactly = 1, timeout = 1000) {
            listener.onMessage(any(), any())
        }
    }
}