package com.github.sleuthrabbit

import brave.messaging.MessagingRequest
import brave.sampler.SamplerFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.sleuth.instrument.messaging.ConsumerSampler
import org.springframework.cloud.sleuth.instrument.messaging.ProducerSampler
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.IntStream

@SpringBootApplication
class SleuthRabbitApplication {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SleuthRabbitApplication::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SleuthRabbitApplication>(*args)
        }
    }

    @Bean
    fun supplyMessages(): Supplier<Flux<Message<*>>> {
        return Supplier {
            Flux.fromIterable<Message<*>>(
                    IntStream.range(1, 100).boxed()
                            .map {
                                MessageBuilder.withPayload("Message $it").build()
                            }.collect(Collectors.toList())
            )
        }
    }

    @Bean
    fun receiveMessages(): Consumer<Message<Any>> {
        return Consumer { message ->
            LOG.info("Received b3 header: ${message.headers["b3"]}")
        }
    }

    @Bean
    @ConsumerSampler
    @ProducerSampler
    fun samplerFunction(): SamplerFunction<MessagingRequest> {
        return SamplerFunction { false }
    }

}


