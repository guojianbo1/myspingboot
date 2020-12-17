package com.example.myspringboot.controller;

import com.example.myspringboot.mode.HelloWord;
import com.example.myspringboot.service.HelloWordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HelloWordController {
    HelloWordService service = new HelloWordService();

    @RequestMapping("/sayhello")
    public HelloWord sayHelloWord(){
        return service.sayHelloWord();
    }

    @RequestMapping("/test")
    public String test(@RequestParam String aaa){
        return service.eee();
    }
}
