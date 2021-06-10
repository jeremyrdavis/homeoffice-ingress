package io.quarkuscoffeeshop.homeoffice.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RegisterForReflection
public class JsonUtil {

    static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    static String toJson(final Object object) {
        logger.debug("marshalling {} to JSON", object.toString());
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "{ \"error\" : \"" + e.getMessage() + "\" }";
        }
    }

}
