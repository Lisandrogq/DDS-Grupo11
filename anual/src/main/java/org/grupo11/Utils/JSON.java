
package org.grupo11.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parse(String json, Class<T> valueType) throws Exception {
        return objectMapper.readValue(json, valueType);
    }

    public static String stringify(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
