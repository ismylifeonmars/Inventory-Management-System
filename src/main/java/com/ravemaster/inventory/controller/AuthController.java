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

        User userByEmail = userService.getUserByEmail(userDetails.getUsername());
        UserDto userDto = userMapper.toDto(userByEmail);

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
                .user(userDto)
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Response> register(
            @RequestBody RegisterRequest registerRequest
            ){

        //Save user in Database
        User createdUser = userService.createUser(registerRequest);

        //Authenticate user
        UserDetails userDetails = authenticationService.authenticate(
                createdUser.getEmail(), registerRequest.getPassword()
        );

        //Generate token
        String token = authenticationService.generateToken(userDetails);

        User userByEmail = userService.getUserByEmail(userDetails.getUsername());
        UserDto userDto = userMapper.toDto(userByEmail);


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
                .user(userDto)
                .build();
        return ResponseEntity.ok(registerResponse);
    }

}
