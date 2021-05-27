package io.quarkuscoffeeshop.homeoffice.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;


public class Order {

    static Logger logger = LoggerFactory.getLogger(Order.class);


    public String orderId;

    public String orderSource;

    public EventType eventType;

    public String loyaltyMemberId;

    public Instant timestamp;


    private List<LineItem> baristaLineItems;


    private List<LineItem> kitchenLineItems;

    public Order(final String orderId, final String orderSource, final EventType eventType, final Instant instant, final Optional<String> loyaltyMemberId, Optional<List<LineItem>> baristaLineItems, Optional<List<LineItem>> kitchenLineItems) {
        this.orderId = orderId;
        this.orderSource = orderSource;
        this.eventType = eventType;
        this.timestamp = instant;
        if (loyaltyMemberId.isPresent()) {
            this.loyaltyMemberId = loyaltyMemberId.get();
        }else{
            this.loyaltyMemberId = null;
        }
        if (baristaLineItems.isPresent()) {
            baristaLineItems.get().forEach(baristaLineItem -> {
                addBaristaLineItem(new LineItem(baristaLineItem.getItem(), baristaLineItem.getName(), this));
            });
        }
        if (kitchenLineItems.isPresent()) {
            baristaLineItems.get().forEach(kitchenLineItem -> {
                addKitchenLineItem(new LineItem(kitchenLineItem.getItem(), kitchenLineItem.getName(), this));
            });
        }
    }

    public Order() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("orderSource='" + orderSource + "'")
                .add("eventType='" + eventType + "'")
                .add("loyaltyMemberId='" + loyaltyMemberId + "'")
                .add("timestamp=" + timestamp)
                .add("baristaLineItems=" + baristaLineItems)
                .add("kitchenLineItems=" + kitchenLineItems)
                .toString();
    }

    /**
     * Convenience method to prevent Null Pointer Exceptions
     * @param lineItem
     */
    public void addBaristaLineItem(final LineItem lineItem) {
        if (this.baristaLineItems == null) {
            this.baristaLineItems = new ArrayList<>();
        }
        this.baristaLineItems.add(new LineItem(lineItem.getItem(), lineItem.getName(), this));
    }

    /**
     * Convenience method to prevent Null Pointer Exceptions
     * @param lineItem
     */
    public void addKitchenLineItem(final LineItem lineItem) {
        if (this.kitchenLineItems == null) {
            this.kitchenLineItems = new ArrayList<>();
        }
        this.kitchenLineItems.add(new LineItem(lineItem.getItem(), lineItem.getName(), this));
    }

    public Optional<List<LineItem>> getBaristaLineItems() {
        return Optional.ofNullable(baristaLineItems);
    }

    public Optional<List<LineItem>> getKitchenLineItems() {
        return Optional.ofNullable(kitchenLineItems);
    }

    public Optional<String> getLoyaltyMemberId() {
        return Optional.ofNullable(this.loyaltyMemberId);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setBaristaLineItems(List<LineItem> baristaLineItems) {
        baristaLineItems.forEach(baristaLineItem -> {
            addBaristaLineItem(baristaLineItem);
        });
    }

    public void setKitchenLineItems(List<LineItem> kitchenLineItems) {
        kitchenLineItems.forEach(kitchenLineItem -> {
            addKitchenLineItem(kitchenLineItem);
        });
    }
}
