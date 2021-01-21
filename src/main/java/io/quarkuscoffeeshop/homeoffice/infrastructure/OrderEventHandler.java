package io.quarkuscoffeeshop.homeoffice.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkuscoffeeshop.homeoffice.domain.Order;
import io.quarkuscoffeeshop.homeoffice.messagelog.MessageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class OrderEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OrderEventHandler.class);

//    @Inject
//    MessageLog log;

    @Inject
    OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void onOrderEvent(UUID eventId, String eventType, String key, String event, Instant ts) {
//        if (log.alreadyProcessed(eventId)) {
//            LOG.info("Event with UUID {} was already retrieved, ignoring it", eventId);
//            return;
//        }

        JsonNode eventPayload = deserialize(event);

        LOG.info("Received 'Order' event -- key: {}, event id: '{}', event type: '{}', ts: '{}'", key, eventId, eventType, ts);

        switch (eventType) {
            case "OrderCreated":
                LOG.info("OrderCreated event received");
                orderService.orderCreated(eventPayload);
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
//        log.processed(eventId);
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


}
