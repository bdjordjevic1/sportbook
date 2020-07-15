package com.ronin.sportbook.common.security.controller;


import com.ronin.sportbook.common.controller.BaseApiController;
import com.ronin.sportbook.common.security.exception.BadRequestException;
import com.ronin.sportbook.common.security.model.AuthProvider;
import com.ronin.sportbook.common.security.payload.ApiResponse;
import com.ronin.sportbook.common.security.payload.AuthResponse;
import com.ronin.sportbook.common.security.payload.LoginRequest;
import com.ronin.sportbook.common.security.payload.SignUpRequest;
import com.ronin.sportbook.common.security.service.TokenProviderService;
import com.ronin.sportbook.repository.UserRepository;
import com.ronin.sportbook.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException(getMessageSource().getMessage("email.alreadyExists", null, Locale.ENGLISH));
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

        return new ApiResponse(true, "User registered successfully!", "user.registration.success");
    }

}
