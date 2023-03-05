package org.project.server.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestData {
    private Object body;
    private Map<String, Object> queryParams;
    private Map<String, String> pathParams;
}
