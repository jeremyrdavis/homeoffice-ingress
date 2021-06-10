package io.quarkuscoffeeshop.homeoffice.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkuscoffeeshop.homeoffice.domain.Order;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;

import static io.quarkuscoffeeshop.homeoffice.infrastructure.JsonUtil.toJson;

@RegisterForReflection
@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-created")
    Emitter<String> ordersCreatedEmitter;

    @Inject
    @Channel("orders-updated")
    Emitter<String> ordersUpdatedEmitter;

    @Inject
    @Channel("loyalty-member-purchase")
    Emitter<String> loyaltyMemberPurchaseEmitter;

    public CompletableFuture<Void> addOrderCreated(final Order order) {
        logger.debug("eventType: {}", order.getEventType());
        logger.debug("Order: {}", order);
        logger.debug("Sending json: {}", toJson(order));

        return ordersCreatedEmitter.send(toJson(order))
                .whenComplete((result, ex) -> {
                    logger.debug("order added: {}", order);
                    if (ex != null) {
                        logger.error(ex.getMessage());
                    }
                }).toCompletableFuture();
    }


    public CompletableFuture<Void> addOrderUpdated(final Order order) {
        logger.debug("eventType: {}", order.getEventType());
        logger.debug("Order: {}", order);

        return ordersUpdatedEmitter.send(toJson(order))
                .whenComplete((result, ex) -> {
                    logger.debug("order updated: {}", order);
                    if (ex != null) {
                        logger.error(ex.getMessage());
                    }
                }).toCompletableFuture();
    }

    public CompletableFuture<Void> addLoyaltyMemberPurchase(final Order order) {
        logger.debug("eventType: {}", order.getEventType());
        logger.debug("Order: {}", order);

        return loyaltyMemberPurchaseEmitter.send(toJson(order))
                .whenComplete((result, ex) -> {
                    logger.debug("loyalty member purchase added: {}", order);
                    if (ex != null) {
                        logger.error(ex.getMessage());
                    }
                }).toCompletableFuture();
    }
}