package io.quarkuscoffeeshop.homeoffice.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        LOG.info("Received 'Order' event -- key: {}, event id: '{}', event type: '{}', ts: '{}'", key, eventId, eventType, ts);

        JsonNode eventPayload = deserialize(event);

        if (eventType.equals("OrderCreated")) {
            orderService.orderCreated(eventPayload);
        }
        else {
            LOG.info("we got a LoyaltyMemberPurchaseEvent!");
        }
    }

    private JsonNode deserialize(String event) {
        JsonNode eventPayload;
        LOG.info("event: {}", event);

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
