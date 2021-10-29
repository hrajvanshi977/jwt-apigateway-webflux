package com.jwtgateway.project.controller;

import com.jwtgateway.project.dto.UserDto;
import com.jwtgateway.project.dto.mapper.UserMapper;
import com.jwtgateway.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final UserMapper userMapper;

    public PublicController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/demo-user")
    public Mono<UserDto> newUser(@RequestBody UserDto userDto) {
        var user = userMapper.map(userDto);
        return userService.createUser(user)
                .map(u -> userMapper.map(u));
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello-world!");
    }

    @GetMapping("/hello-world")
    public Mono<String> helloWorld() {
        return Mono.just("Hello-world india!");
    }

    @GetMapping("/version")
    public Mono<String> version() {
        return Mono.just("1.0.0");
    }
}