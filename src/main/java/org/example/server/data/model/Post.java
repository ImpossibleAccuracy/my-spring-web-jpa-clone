package org.example.server.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Integer id;
    private String title;
    private String content;
    private Instant created;

    private User author;
}
