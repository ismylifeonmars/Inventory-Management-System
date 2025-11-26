package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.UserDto;
import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.UpdateUserRequest;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.mapper.UserMapper;
import com.ravemaster.inventory.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getUser(
            @PathVariable("id") UUID uuid
    ){
        User userById = userService.getUserById(uuid);
        UserDto userDto = userMapper.toDto(userById);
        Response response = Response.builder()
                .status(200)
                .message("Success")
                .user(userDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateUser(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
            ){
        User user = userService.updateUser(id, updateUserRequest);
        UserDto userDto = userMapper.toDto(user);
        Response response = Response.builder()
                .status(200)
                .message("Success")
                .user(userDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteUser(
            @PathVariable("id") UUID id
    ){
        userService.deleteUser(id);
        Response response = Response.builder()
                .status(200)
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllUsers(
    ){
        List<UserDto> allUsers = userService.getAllUsers();
        Response response = Response.builder()
                .status(200)
                .message("Success")
                .users(allUsers)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
