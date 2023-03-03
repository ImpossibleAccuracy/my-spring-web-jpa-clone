package com.project.server.database.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.project.server.database.ConnectionInfo;

import java.io.IOException;
import java.io.InputStream;

public class DatabasePropertyLoader {
    public static ConnectionInfo loadFromResource(String resourceName) throws IOException {
        InputStream resource = getResource(resourceName);

        if (resource == null) throw new ResourceNotFoundException(resourceName);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        YamlSchema schema = mapper.readValue(resource, YamlSchema.class);
        return schema.database();
    }

    private static InputStream getResource(String resourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resourceName);
    }
}
