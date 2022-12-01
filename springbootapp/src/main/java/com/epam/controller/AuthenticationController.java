package com.epam.controller;

import io.micrometer.core.annotation.Timed;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.AuthenticationRequestDto;
import com.epam.model.User;
import com.epam.security.jwt.JwtTokenProvider;
import com.epam.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Timed(value = "user.login.time", description = "Time to login a user", percentiles = {0.5, 0.9})
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            Optional<User> user = userService.findByUsername(username);

            if (user.isEmpty()) {
                throw new UsernameNotFoundException("No user found with such username");
            }

            String token = jwtTokenProvider.createToken(username, user.get().getRoles());

            return new ResponseEntity<>(Map.of("username", username, "token", token), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid creds");
        }
    }

}
