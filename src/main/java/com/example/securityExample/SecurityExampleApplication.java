package com.example.securityExample;

import com.example.securityExample.model.Role;
import com.example.securityExample.model.User;
import com.example.securityExample.repository.UserRepository;
import com.example.securityExample.service.ReaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SecurityExampleApplication {

    private final UserRepository userRepository;
    private final ReaderService readerService;

    public SecurityExampleApplication(UserRepository userRepository, ReaderService readerService) {
        this.userRepository = userRepository;
        this.readerService = readerService;
    }

    //example data in database
    @Bean
    public void addExampleData() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.save(new User("user@gmail.com", encoder.encode("password"), Role.ROLE_USER, readerService.createDefaultReader(), true));
        userRepository.save(new User("admin@gmail.com", encoder.encode("password"), Role.ROLE_ADMIN, readerService.createDefaultReader(), true));
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityExampleApplication.class, args);
    }

}
