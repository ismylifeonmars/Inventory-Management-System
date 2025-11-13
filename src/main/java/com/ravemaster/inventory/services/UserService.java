package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.RegisterRequest;
import com.ravemaster.inventory.domain.request.UpdateUserRequest;

import java.util.UUID;

public interface UserService{
    User createUser(RegisterRequest registerRequest);
    User getUserById(UUID id);
    User getUserByName(String name);
    User updateUser(UUID id, UpdateUserRequest user);
    void deleteUser(UUID id);
}
