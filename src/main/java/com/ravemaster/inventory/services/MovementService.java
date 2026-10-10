package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.MovementDto;
import com.ravemaster.inventory.domain.dto.MovementDtoSecond;
import com.ravemaster.inventory.domain.request.MovementRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MovementService {
    MovementDtoSecond createMovement(MovementRequest movementRequest);
    Page<MovementDto> getMovements(Pageable pageable);
    MovementDtoSecond getMovementWithLines(UUID id);
    void deleteMovement(UUID id);
}
