package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.model.AuthenticationRequest;
import com.unforgettable.securitypart.model.AuthenticationResponse;
import com.unforgettable.securitypart.model.RegistrationRequest;
import com.unforgettable.securitypart.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request){
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<>(authenticationService.refreshToken(request, response), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RegistrationRequest request){
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.OK);
    }

    @GetMapping
    public String hello(){
        return "Hello";
    }
}
