package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ApplicationOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final ApplicationUserDetailsService userDetailsService;

    @Autowired
    public ApplicationOAuth2UserService(ApplicationUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        // extract the user's name
        String username = (String) userAttributes.get("login");

        // check if the user already exists in the database
        UserEntity existingUser = userDetailsService.getUserByUsername(username);
        if (existingUser != null) {

            DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                    Set.of(new SimpleGrantedAuthority("ROLE_" + existingUser.getRole().name())),
                    userAttributes, "login");
            return defaultOAuth2User;
        }

        // create a new user and save it to the database;
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setRole(UserRole.STUDENT);
        userDetailsService.saveUser(newUser);
        OAuth2UserAuthority authority = new OAuth2UserAuthority(userAttributes);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);
        return new DefaultOAuth2User(authorities,
                userAttributes, "email");

    }
}
