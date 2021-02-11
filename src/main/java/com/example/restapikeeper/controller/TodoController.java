package com.example.restapikeeper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TodoController {

    @GetMapping("/api/todos")
    @ResponseBody
    public String getTodos() {
        log.info("hihi");
        return "todo";
    }
}
