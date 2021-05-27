package io.quarkuscoffeeshop.homeoffice.infrastructure;

import io.quarkuscoffeeshop.homeoffice.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    OrderService orderService;

    @POST
    @Path("order")

    public Response add(Order order){

        switch (order.eventType) {
            case OrderCreated:
                orderService.addOrderCreated(order);
                break;
            case OrderUpdated:
                orderService.addOrderUpdated(order);
                break;
            case LoyaltyMemberPurchase:
                orderService.addLoyaltyMemberPurchase(order);
                break;
            default:
                LOG.error("Cannot determine appropriate action for {}", order.eventType);
        }

        return Response.accepted().build();

    }

}
