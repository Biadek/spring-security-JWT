package com.example.securityExample.service;

import com.example.securityExample.dto.LoginDto;
import com.example.securityExample.dto.LoginResponseDto;
import com.example.securityExample.exception.ActivationTokenNotFoundException;
import com.example.securityExample.exception.ConflictException;
import com.example.securityExample.exception.EmailSenderException;
import com.example.securityExample.exception.UserNotFoundException;
import com.example.securityExample.model.ActivationToken;
import com.example.securityExample.model.Reader;
import com.example.securityExample.model.Role;
import com.example.securityExample.model.User;
import com.example.securityExample.repository.ActivationTokenRepository;
import com.example.securityExample.repository.UserRepository;
import com.example.securityExample.security.JwtToken;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ReaderService readerService;
    private JwtToken jwtToken;
    private AuthenticationManager authenticationManager;
    private MailService mailService;
    private ActivationTokenRepository tokenRepository;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       ReaderService readerService, PasswordEncoder passwordEncoder, JwtToken jwtToken,
                       MailService mailService, ActivationTokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.readerService = readerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtToken = jwtToken;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void registerNewUser(LoginDto loginDto) {
        if (userRepository.existsByEmail(loginDto.getEmail())) {
            throw new ConflictException(String.format("User with email: %s already exists", loginDto.getEmail()));
        }

        User user = userRepository.save(new User(
                loginDto.getEmail(),
                passwordEncoder.encode(loginDto.getPassword()),
                Role.ROLE_USER,
                readerService.createDefaultReader(),
                false
        ));
        sendToken(user);
    }

    private void sendToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        ActivationToken token = new ActivationToken(tokenValue, user);
        tokenRepository.save(token);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String activationLink = baseUrl + "/user/token?value=" + tokenValue;

        try {
            mailService.sendMail(user.getEmail(), "Activate your account!", activationLink, false);
        } catch (MessagingException | MailException e) {
            throw new EmailSenderException(e.getMessage());
        }
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
                .orElseThrow(() -> new UserNotFoundException(principal.getUsername()));
    }

    public Reader getCurrentReader() {
        User user = getCurrentUser();
        return user.getReader();
    }

    public Role getRole() {
        User user = getCurrentUser();
        return user.getRole();
    }

    public void setReaderRoleAdmin(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    public void setReaderRoleUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    public void activateAccount(String value) {
        ActivationToken token = tokenRepository.findByToken(value).orElseThrow(() -> new ActivationTokenNotFoundException(value));
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}
