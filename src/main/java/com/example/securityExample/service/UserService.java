package com.example.securityExample.service;

import com.example.securityExample.dto.LoginDto;
import com.example.securityExample.dto.LoginResponseDto;
import com.example.securityExample.exception.ConflictException;
import com.example.securityExample.exception.NotFoundException;
import com.example.securityExample.model.Reader;
import com.example.securityExample.model.Role;
import com.example.securityExample.model.User;
import com.example.securityExample.repository.UserRepository;
import com.example.securityExample.security.JwtToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ReaderService readerService;
    private JwtToken jwtToken;
    private AuthenticationManager authenticationManager;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, ReaderService readerService, PasswordEncoder passwordEncoder, JwtToken jwtToken) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.readerService = readerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtToken = jwtToken;
    }

    public void registerNewUser(LoginDto loginDto) {
        if (userRepository.existsByEmail(loginDto.getEmail())) {
            throw new ConflictException(String.format("User with email: %s already exists", loginDto.getEmail()));
        }

        userRepository.save(new User(
                loginDto.getEmail(),
                passwordEncoder.encode(loginDto.getPassword()),
                Role.ROLE_USER,
                readerService.createDefaultReader()
        ));
    }

    public LoginResponseDto loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponseDto(loginDto.getEmail(), jwtToken.generateToken(authentication));
    }

    private User getCurrentUser() {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) contextHolder.getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found with username: " + principal.getUsername()));
    }

    public Reader getCurrentReader() {
        User user = getCurrentUser();
        return user.getReader();
    }

    public Role getRole() {
        User user = getCurrentUser();
        return user.getRole();
    }
}
