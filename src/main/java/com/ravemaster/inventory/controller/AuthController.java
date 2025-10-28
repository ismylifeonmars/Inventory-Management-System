package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.UserDto;
import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.LoginRequest;
import com.ravemaster.inventory.domain.request.RegisterRequest;
import com.ravemaster.inventory.domain.response.AuthResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.mapper.UserMapper;
import com.ravemaster.inventory.services.AuthenticationService;
import com.ravemaster.inventory.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(path = "/login")
    public ResponseEntity<Response> login(
            @RequestBody LoginRequest loginRequest
    ){
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        String token = authenticationService.generateToken(userDetails);
        Response authResponse = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .authResponse(
                        AuthResponse.builder()
                                .token(token)
                                .expiryDateMillis(60L * 60L * 24L * 1000L)
                                .build()
                )
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Response> register(
            @RequestBody RegisterRequest registerRequest
            ){

        //Create UserDto
        UserDto userDto = UserDto.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .role(registerRequest.getRole())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();

        //Encode password and set it to dto
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //Save user in Database
        User createdUser = userService.createUser(userMapper.toEntity(userDto));

        //Authenticate user
        UserDetails userDetails = authenticationService.authenticate(
                createdUser.getEmail(), registerRequest.getPassword()
        );

        //Generate token
        String token = authenticationService.generateToken(userDetails);

        //Return response
        Response registerResponse = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Success")
                .authResponse(
                        AuthResponse.builder()
                                .token(token)
                                .expiryDateMillis(60L * 60L * 24L * 1000L)
                                .build()
                )
                .build();
        return ResponseEntity.ok(registerResponse);
    }

}
