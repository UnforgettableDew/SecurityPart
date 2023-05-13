package com.unforgettable.securitypart.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @GetMapping
    public String greeting(){
        return "Hello Diploma";
    }
    @GetMapping("/github")
    public String github(){
        return "Hello Github";
    }

}
