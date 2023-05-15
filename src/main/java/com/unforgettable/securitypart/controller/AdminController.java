package com.unforgettable.securitypart.controller;

import com.unforgettable.securitypart.entity.Educator;
import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@EnableMethodSecurity
@RequestMapping("/admin")
public class AdminController {
    private final ApplicationUserRepository applicationUserRepository;
    @Autowired
    public AdminController(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("{username}")
//    public UserEntity getUserEntity(@PathVariable String username){
//        return applicationUserRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username=" + username + " not found");
//    }

    @GetMapping("/hello")
    public ResponseEntity<String> admin(){
        return ResponseEntity.ok("Hello admin");
    }

//    @GetMapping("/educator/{username}")
//    public Educator getEducatorByUsername(@PathVariable String username){
//        return applicationUserRepository.findByUsername(username).getEducator();
//    }
}
