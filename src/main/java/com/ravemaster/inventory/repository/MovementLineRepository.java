package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.MovementLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovementLineRepository extends JpaRepository<MovementLine, UUID> {
}
