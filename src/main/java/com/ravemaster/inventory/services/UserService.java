package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.UpdateUserRequest;

import java.util.UUID;

public interface UserService{
    User createUser(User user);
    User getUserById(UUID id);
    User updateUser(UUID id, UpdateUserRequest user);
    void deleteUser(UUID id);
}
