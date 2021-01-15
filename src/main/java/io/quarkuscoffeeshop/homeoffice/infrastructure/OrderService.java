package io.quarkuscoffeeshop.homeoffice.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value= Transactional.TxType.MANDATORY)
    public void orderCreated(JsonNode event) {

        LOGGER.info("Processing 'OrderCreated' event: {}", event);

    }
}
