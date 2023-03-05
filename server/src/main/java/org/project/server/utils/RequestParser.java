package org.project.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> parseGetParams(String query) {
        if (query == null || query.trim().equals("")) return Map.of();

        Map<String, Object> result = new HashMap<>();

        String[] queryParts = query.split("&");
        for (String expression : queryParts) {
            if (expression.equals("")) continue;

            String[] expressionParts = expression.split("=", 2);

            String name = expressionParts[0];
            Object value;

            if (expressionParts.length == 1) {
                value = null;
            } else {
                try {
                    value = NumberFormat.getInstance().parse(expressionParts[1]);
                } catch (ParseException e) {
                    value = expressionParts[1];
                }
            }

            result.put(name, value);
        }

        return result;
    }

    public Map<String, String> parsePathParams(String path, String actualUrl) {
        return UrlUtils.retrievePathVariables(path, actualUrl);
    }

    public <B> B parseBody(InputStream body, Class<B> bClass) throws IOException {
        if (body.available() == 0) return null;

        return mapper.readValue(body, bClass);
    }
}
