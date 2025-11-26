package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.transactions WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.transactions WHERE u.name = :name")
    Optional<User> findByName(@Param("name") String name);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.transactions")
    List<User> getAllUsers();
}
