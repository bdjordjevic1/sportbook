package com.ronin.sportbook.common.security.controller;


import com.ronin.sportbook.common.controller.BaseApiController;
import com.ronin.sportbook.common.security.payload.ApiResponse;
import com.ronin.sportbook.common.security.payload.AuthResponse;
import com.ronin.sportbook.common.security.payload.LoginRequest;
import com.ronin.sportbook.common.security.payload.SignUpRequest;
import com.ronin.sportbook.common.security.exception.BadRequestException;
import com.ronin.sportbook.common.security.model.AuthProvider;
import com.ronin.sportbook.common.security.service.TokenProviderService;
import com.ronin.sportbook.repository.UserRepository;
import com.ronin.sportbook.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProviderService tokenProviderService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProviderService.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        UserModel user = new UserModel();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setAuthProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserModel result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/login")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location)
                             .body(new ApiResponse(true, "User registered successfully!"));
    }

}
