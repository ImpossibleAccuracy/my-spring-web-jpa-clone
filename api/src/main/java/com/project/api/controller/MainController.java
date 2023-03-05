package com.project.api.controller;

import com.project.api.data.payload.MainRequestData;
import org.project.server.HttpController;
import org.project.server.annotation.GetParam;
import org.project.server.annotation.RequestBody;
import org.project.server.annotation.route.GetMapping;
import org.project.server.annotation.route.PostMapping;
import org.project.server.annotation.route.RequestMapping;
import org.project.server.exception.http.BadRequestException;

@RequestMapping("/main")
public class MainController extends HttpController {
    @GetMapping
    @RequestMapping
    public String mainRoute() {
        throw new NullPointerException();
        // return "Hello, Client!";
    }

    @PostMapping
    @RequestMapping("/update/")
    public MainRequestData subRoute(@GetParam("newId") Integer newId,
                                    @RequestBody MainRequestData requestData) {
        if (requestData == null) throw new BadRequestException("Request data must be not null");
        if (newId == null) throw new BadRequestException("NewId must be not null");

        requestData.setId(newId);

        return requestData;
    }
}
