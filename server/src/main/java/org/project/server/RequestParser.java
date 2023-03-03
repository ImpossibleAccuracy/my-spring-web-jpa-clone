package org.project.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.project.server.utils.UrlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> parsePathParams(String path, String actualUrl) {
        return UrlUtils.getPathVariables(path, actualUrl);
    }

    public <B> B parseBody(InputStream body, Class<B> bClass) throws IOException {
        if (body.available() == 0) return null;

        return mapper.readValue(body, bClass);
    }

    public Map<String, String> parseGetParams(String query) {
        if (query == null || query.trim().equals("")) return Map.of();

        Map<String, String> result = new HashMap<>();

        String[] queryParts = query.split("&");
        for (String expression : queryParts) {
            if (expression.equals("")) continue;

            String[] expressionParts = expression.split("=", 2);

            if (expressionParts.length == 1) {
                result.put(expressionParts[0], null);
            } else {
                result.put(expressionParts[0], expressionParts[1]);
            }
        }

        return result;
    }
}
