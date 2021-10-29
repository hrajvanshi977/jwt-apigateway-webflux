package com.jwtgateway.project.configuration.security;

import com.jwtgateway.project.configuration.security.auth.UnauthorizedException;
import com.jwtgateway.project.configuration.security.auth.UserPrincipal;
import com.jwtgateway.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/*
 @author Erik Amaru Ortiz
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private UserService userService;

    public AuthenticationManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return userService.getUser(principal.getId())
                .filter(user -> user.isEnabled())
                .switchIfEmpty(Mono.error(new UnauthorizedException("User account is disabled.")))
                .map(user -> authentication);
    }
}