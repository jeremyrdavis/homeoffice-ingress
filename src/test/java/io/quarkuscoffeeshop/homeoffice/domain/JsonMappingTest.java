package io.quarkuscoffeeshop.homeoffice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonMappingTest {

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testOrderJsonMapping() {
        String json = "{\"orderId\":\"fa280ea4-1b92-4f26-8df4-3b11d38e38dd\",\"orderSource\":\"WEB\",\"timestamp\":\"2021-01-20T13:54:25.914187Z\",\"baristaLineItems\":[{\"item\":\"COFFEE_BLACK\",\"name\":\"Paul\"}]}";

        try {
            Order order = objectMapper.readValue(json, Order.class);
            assertEquals("fa280ea4-1b92-4f26-8df4-3b11d38e38dd", order.getOrderId());
            assertEquals("WEB", order.getOrderSource());
            assertEquals(1, order.getBaristaLineItems().get().size());
        } catch (JsonProcessingException e) {
            assertNull(e);
        }
    }
}
