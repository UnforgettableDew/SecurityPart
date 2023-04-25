package com.unforgettable.securitypart.controller;


import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class TestController {
    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public TestController(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @GetMapping("{username}")
    public UserEntity getUserEntity(@PathVariable String username){
        return applicationUserRepository.findByUsername(username).get();
    }
}
