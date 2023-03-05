package com.project.api.data.payload;

import lombok.Data;

import java.util.List;

@Data
public class MainRequestData {
    private String username;
    private Integer id;
    private List<String> words;
}
