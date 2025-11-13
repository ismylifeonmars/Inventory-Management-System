package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.UserDto;
import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.RegisterRequest;
import com.ravemaster.inventory.domain.request.UpdateUserRequest;
import com.ravemaster.inventory.mapper.UserMapper;
import com.ravemaster.inventory.repository.UserRepository;
import com.ravemaster.inventory.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createUser(RegisterRequest registerRequest) {

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

        String username = userDto.getEmail();

        if (userRepository.existsByEmail(username)){
            throw new IllegalArgumentException("User already exists with email: "+username);
        }
        User entity = userMapper.toEntity(userDto);

        return userRepository.save(entity);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                "User not found with id: "+ id
        ));
    }

    @Override
    @Transactional
    public User updateUser(UUID id, UpdateUserRequest user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with id:"+ id
                ));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);

    }
}
