package com.project.server.database.loader;

import com.project.server.database.ConnectionInfo;

public record YamlSchema(
        ConnectionInfo database
) {
}
