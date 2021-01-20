package io.quarkuscoffeeshop.homeoffice.domain;



import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "Orders")
public class Order extends PanacheEntityBase {

    @Transient
    static Logger logger = LoggerFactory.getLogger(Order.class);

    @Id
    @Column(nullable = false, name = "orderId")
    public String orderId;

    public String orderSource;

    public String loyaltyMemberId;

    public LocalDateTime timestamp;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
//    private List<LineItem> baristaLineItems;
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
//    private List<LineItem> kitchenLineItems;

//    /**
//     * Convenience method to prevent Null Pointer Exceptions
//     * @param lineItem
//     */
//    public void addBaristaLineItem(final LineItem lineItem) {
//        if (this.baristaLineItems == null) {
//            this.baristaLineItems = new ArrayList<>();
//        }
//        this.baristaLineItems.add(lineItem);
//    }
//
//    /**
//     * Convenience method to prevent Null Pointer Exceptions
//     * @param lineItem
//     */
//    public void addKitchenLineItem(final LineItem lineItem) {
//        if (this.kitchenLineItems == null) {
//            this.kitchenLineItems = new ArrayList<>();
//        }
//        this.kitchenLineItems.add(lineItem);
//    }

//    public Optional<List<LineItem>> getBaristaLineItems() {
//        return Optional.ofNullable(baristaLineItems);
//    }
//
//    public Optional<List<LineItem>> getKitchenLineItems() {
//        return Optional.ofNullable(kitchenLineItems);
//    }

//    public Optional<String> getLoyaltyMemberId() {
//        return Optional.ofNullable(this.loyaltyMemberId);
//    }
//
//    public void setLoyaltyMemberId(String loyaltyMemberId) {
//        this.loyaltyMemberId = loyaltyMemberId;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getOrderSource() {
//        return orderSource;
//    }
//
//    public void setOrderSource(String orderSource) {
//        this.orderSource = orderSource;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }

    public Order() {

    }

}
