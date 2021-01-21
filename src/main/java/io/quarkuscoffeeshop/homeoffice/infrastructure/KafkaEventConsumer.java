package io.quarkuscoffeeshop.homeoffice.infrastructure;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkuscoffeeshop.homeoffice.domain.Order;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@ApplicationScoped
public class KafkaEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaEventConsumer.class);

    @Inject
    OrderEventHandler orderEventHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Incoming("orders")
    public CompletionStage<Void> onMessage(KafkaRecord<String, String> message) throws IOException {
        return CompletableFuture.runAsync(() -> {

            //LOG.debug("Kafka message with key = {} arrived", message.getKey());
            LOG.debug("message received: {}", message.getPayload());

            String eventId = getHeaderAsString(message, "id");
            String eventType = getHeaderAsString(message, "eventType");

            LOG.debug("EventType is: {}", eventType);

            LOG.debug("Payload: {}", message.getPayload());

            try {
                Order order = objectMapper.readValue(message.getPayload(), Order.class);
                LOG.debug("order: {}", order);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
/*
            JsonNode eventPayload = deserialize(message.getPayload());
            LOG.debug("eventPayload: {}", eventPayload);

            LOG.info("Received 'Order' event -- key: {}, event id: '{}', event type: '{}', ts: '{}'", UUID.fromString(eventId), eventId, eventType, Instant.now());

            switch (eventType) {
                case "OrderCreated":
                    LOG.info("OrderCreated event received");
                    //orderService.orderCreated(eventPayload);
                    break;
                case "OrderUpdated" :
                    LOG.info("OrderUpdated event received");
                    break;

                case "LoyaltyMemberPurchaseEvent" :
                    LOG.info("LoyaltyMemberPurchaseEvent event received");
                    break;

                default :
                    LOG.info("Unrecognized event received");
                    break;
            }

            orderEventHandler.onOrderEvent(
                    UUID.fromString(eventId),
                    eventType,
                    message.getKey(),
                    message.getPayload(),
                    message.getTimestamp()
            );
*/


        }).thenRun(message::ack);

    }

    private JsonNode deserialize(String event) {
        JsonNode eventPayload;

        try {
            //String unescaped = objectMapper.readValue(event, String.class);
            eventPayload = objectMapper.readTree(event);
        }
        catch (IOException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }

        return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
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


