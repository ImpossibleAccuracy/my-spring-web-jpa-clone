package com.project.api.controller;

import org.project.server.annotation.PathVariable;
import org.project.server.annotation.route.GetMapping;

public class MainController {
    @GetMapping
    public String mainRoute() {
        return "Hello, Client!";
    }
    @GetMapping("/{param}")
    public String subRoute(@PathVariable("param") String param) {
        return "Hello from subroute! Path param: %s.".formatted(param);
    }
}
