package com.unforgettable.securitypart.config;

import com.unforgettable.securitypart.security.JwtAuthenticationFilter;
import com.unforgettable.securitypart.service.ApplicationOAuth2UserService;
import com.unforgettable.securitypart.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SecurityConfig {

    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final ApplicationOAuth2UserService oAuth2UserService;

    @Autowired
    public SecurityConfig(ApplicationUserDetailsService applicationUserDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.applicationUserDetailsService = applicationUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
//                .requestMatchers("/security/**").permitAll
//                .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/auth/**", "/greeting").permitAll()
                    .requestMatchers( "/admin/**").hasRole("EDUCATOR")
                .anyRequest()
                .authenticated()
                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/greeting")
                    .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
//                .and()
//                .oauth2Login().userInfoEndpoint().userService(oAuth2UserService);
        return httpSecurity.build();
    }

    @Bean
    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(applicationUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
