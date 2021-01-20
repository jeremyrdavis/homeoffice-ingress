package io.quarkuscoffeeshop.homeoffice.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkuscoffeeshop.homeoffice.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static javax.transaction.Transactional.TxType;


@ApplicationScoped
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void orderCreated(JsonNode event) {


        Order order = new Order();
        order.orderId = event.get("orderId").asText();
        order.orderSource = event.get("orderSource").asText();
        order.timestamp = LocalDateTime.parse(event.get("timestamp").asText());
        order.loyaltyMemberId = event.get("loyaltyMemberId").asText();
        LOGGER.info("orderId: {}", order.orderId);
        entityManager.persist(order);

        LOGGER.info("Processed 'OrderCreated' event: {}", event);
    }
}
