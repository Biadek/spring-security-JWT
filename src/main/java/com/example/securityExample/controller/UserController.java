package com.example.securityExample.controller;

import com.example.securityExample.dto.LoginDto;
import com.example.securityExample.dto.LoginResponseDto;
import com.example.securityExample.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@RequestBody LoginDto loginDto) {
        userService.registerNewUser(loginDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.loginUser(loginDto));
    }


}
