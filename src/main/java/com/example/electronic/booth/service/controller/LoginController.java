package com.example.electronic.booth.service.controller;

import com.example.electronic.booth.service.bean.Login;
import com.example.electronic.booth.service.bean.Response;
import com.example.electronic.booth.service.bean.TokenResponse;
import com.example.electronic.booth.service.bean.User;
import com.example.electronic.booth.service.repository.UserRepository;
import com.example.electronic.booth.service.security.MyUserDetailsService;
import com.example.electronic.booth.service.security.TokenManager;
import com.example.electronic.booth.service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody Login login) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUserName(), login.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(login.getUserName());
        User user = userRepository.findByEmailId(login.getUserName());
        TokenResponse tokenResponse = new TokenResponse(tokenManager.generateToken(userDetails), user);
        return new ResponseEntity<>(new Response(tokenResponse, 200), HttpStatus.OK);
    }
}
