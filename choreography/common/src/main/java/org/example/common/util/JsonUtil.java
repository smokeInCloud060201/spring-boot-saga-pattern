package org.example.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class JsonUtil {

    private static ObjectMapper mapper;
    private static ObjectWriter writer;

    private static void initialize() {
        if (mapper != null) {
            return;
        }
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper()
                .registerModule(module)
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DefaultPrettyPrinter pp = new DefaultPrettyPrinter()
                .withoutSpacesInObjectEntries()
                .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter())
                .withObjectIndenter(new DefaultPrettyPrinter.NopIndenter());
        writer = mapper.writer().with(pp);
    }

    public static String stringify(Object data) {
        initialize();
        try {
            return writer.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.out.printf("EXCEPTION WHEN PARSE OBJECT TO STRING %s%n", e.getMessage());
        }
        return null;
    }

    public static <T> T getObjectFromJsonString(Class<T> clazz, String json) {
        initialize();
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            System.out.printf("EXCEPTION WHEN PARSE STRING TO OBJECT %s%n", e.getMessage());
        }
        return null;
    }
}
