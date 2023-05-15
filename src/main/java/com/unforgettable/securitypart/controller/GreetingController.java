package com.unforgettable.securitypart.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @GetMapping
    public ResponseEntity<String> greeting(){
        return ResponseEntity.ok("HELLO DIPLOMA");
    }
    @GetMapping("/github")
    public String github(){
        return "Hello Github";
    }

}
