package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.UpdateUserRequest;
import com.ravemaster.inventory.repository.UserRepository;
import com.ravemaster.inventory.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {

        String username = user.getEmail();

        if (userRepository.existsByEmail(username)){
            throw new IllegalArgumentException("User already exists with email: "+username);
        }
        return userRepository.save(user);
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
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);

    }
}
