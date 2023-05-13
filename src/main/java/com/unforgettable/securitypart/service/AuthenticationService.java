package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.enums.UserRole;
import com.unforgettable.securitypart.model.ApplicationUser;
import com.unforgettable.securitypart.model.AuthenticationRequest;
import com.unforgettable.securitypart.model.AuthenticationResponse;
import com.unforgettable.securitypart.model.RegistrationRequest;
import com.unforgettable.securitypart.repository.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 ApplicationUserRepository applicationUserRepository,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String refreshToken = header.substring(7);
        String username = jwtService.extractUsername(refreshToken);

        UserDetails user = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(user);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse register(RegistrationRequest request){
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getUsername()))
                .role(UserRole.STUDENT)
                .build();

        applicationUserRepository.save(user);
        ApplicationUser applicationUser = ApplicationUser.parseEntityUser(user);

        String accessToken = jwtService.generateAccessToken(applicationUser);
        String refreshToken = jwtService.generateRefreshToken(applicationUser);

        return new AuthenticationResponse(accessToken, refreshToken);
    }
}
