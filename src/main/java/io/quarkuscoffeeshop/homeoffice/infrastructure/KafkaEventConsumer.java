package io.quarkuscoffeeshop.homeoffice.infrastructure;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkuscoffeeshop.homeoffice.domain.Order;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class KafkaEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaEventConsumer.class);



    @Inject
    OrderEventHandler orderEventHandler;

    @Incoming("orders")
    public CompletionStage<Void> onMessage(KafkaRecord<String, String> message) throws IOException {
        return CompletableFuture.runAsync(() -> {

            //LOG.debug("Kafka message with key = {} arrived", message.getKey());
            LOG.debug("message received: {}", message.getPayload());

            String eventId = getHeaderAsString(message, "id");
            String eventType = getHeaderAsString(message, "eventType");

            LOG.debug("EventType is: {}",eventType);

            orderEventHandler.onOrderEvent(
                    UUID.fromString(eventId),
                    eventType,
                    message.getKey(),
                    message.getPayload(),
                    message.getTimestamp()
            );


        }).thenRun(message::ack);

    }


//    public CompletionStage<Void> onMessage(KafkaRecord<String, String> message) throws IOException {
//        return CompletableFuture.runAsync(() -> {
//
//            //LOG.debug("Kafka message with key = {} arrived", message.getKey());
//            LOG.debug("message received: {}", message.getPayload());
//
//            String eventId = getHeaderAsString(message, "id");
//            String eventType = getHeaderAsString(message, "eventType");
//
//            LOG.debug("EventType is: {}",eventType);
//
//
//            orderEventHandler.onOrderEvent(
//                    UUID.fromString(eventId),
//                    eventType,
//                    message.getKey(),
//                    message.getPayload(),
//                    message.getTimestamp()
//            );
//
//
//        }).thenRun(message::ack);
//
//    }



    private String getHeaderAsString(KafkaRecord<?, ?> record, String name) {
        Header header = record.getHeaders().lastHeader(name);
        if (header == null) {
            throw new IllegalArgumentException("Expected record header '" + name + "' not present");
        }

        return new String(header.value(), StandardCharsets.UTF_8);
    }


}


