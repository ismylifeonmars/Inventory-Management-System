package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID> {
    @Query("SELECT m.id FROM Movement m")
    Page<UUID> findALlMovementIds(Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movement m LEFT JOIN FETCH m.movementLines LEFT JOIN FETCH m.performedBy u WHERE m.id IN :ids")
    List<Movement> findByIds(@Param("ids") List<UUID> ids, Sort sort);
}
