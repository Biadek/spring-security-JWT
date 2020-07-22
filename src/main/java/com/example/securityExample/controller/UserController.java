package com.example.securityExample.controller;

import com.example.securityExample.dto.LoginDto;
import com.example.securityExample.dto.LoginResponseDto;
import com.example.securityExample.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        userService.registerNewUser(loginDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.loginUser(loginDto));
    }

    @PutMapping("/asAdmin/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> setReaderRoleAdmin(@PathVariable("email") String email) {
        userService.setReaderRoleAdmin(email);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/asUser/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> setReaderRoleUser(@PathVariable("email") String email) {
        userService.setReaderRoleUser(email);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/token")
    public ResponseEntity<Void> activateAccount(@RequestParam String value) {
        userService.activateAccount(value);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
