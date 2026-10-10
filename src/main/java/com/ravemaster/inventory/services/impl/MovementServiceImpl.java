package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.MovementDto;
import com.ravemaster.inventory.domain.dto.MovementDtoSecond;
import com.ravemaster.inventory.domain.dto.MovementLineDto;
import com.ravemaster.inventory.domain.entity.Movement;
import com.ravemaster.inventory.domain.entity.MovementLine;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.entity.User;
import com.ravemaster.inventory.domain.request.MovementLineRequest;
import com.ravemaster.inventory.domain.request.MovementRequest;
import com.ravemaster.inventory.mapper.MovementLineMapper;
import com.ravemaster.inventory.mapper.MovementMapper;
import com.ravemaster.inventory.repository.MovementRepository;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.repository.UserRepository;
import com.ravemaster.inventory.services.MovementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper;
    private final MovementLineMapper movementLineMapper;

    @Override
    public MovementDtoSecond createMovement(MovementRequest movementRequest) {
        Movement movement = Movement.builder()
                .movementType(movementRequest.getMovementType())
                .referenceType(movementRequest.getReferenceType())
                .referenceId(movementRequest.getReferenceId())
                .reason(movementRequest.getReason())
                .build();

        User user = userRepository.findByEmail(movementRequest.getPerformedBy()).orElseThrow(() -> new EntityNotFoundException("Cannot find user with provided email"));

        movement.setPerformedBy(user);

        List<MovementLine> movementLines = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        for (MovementLineRequest movementLineRequest: movementRequest.getMovementLines()){
            Product product = productRepository.findById(movementLineRequest.getProduct()).orElseThrow(() -> new EntityNotFoundException("Cannot find product with provided id"));
            MovementLine movementLine = MovementLine.builder()
                    .quantity(movementLineRequest.getQuantity())
                    .previousQuantity(product.getStockQuantity())
                    .resultingQuantity(product.getStockQuantity()+movementLineRequest.getQuantity())
                    .movement(movement)
                    .product(product)
                    .build();
            product.setStockQuantity(movementLine.getResultingQuantity());
            movementLines.add(movementLine);
        }

        movement.setMovementLines(movementLines);
        productRepository.saveAll(products);

        Movement savedMovement = movementRepository.save(movement);
        List<MovementLine> savedMovementLines = savedMovement.getMovementLines();
        List<MovementLineDto> movementLineDtos = savedMovementLines.stream().map(movementLineMapper::toDto).toList();
        MovementDtoSecond dtoSecond = movementMapper.toDtoSecond(savedMovement);
        dtoSecond.setMovementLines(movementLineDtos);
        return dtoSecond;
    }

    @Override
    public Page<MovementDto> getMovements(Pageable pageable) {
        Page<UUID> ids = movementRepository.findALlMovementIds(pageable);
        return new PageImpl<>(getList(ids,pageable), pageable,ids.getTotalElements());
    }

    @Override
    public MovementDtoSecond getMovementWithLines(UUID id) {
        Movement movement = movementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find stock movement with provided id"));
        List<MovementLine> movementLines = movement.getMovementLines();
        List<MovementLineDto> movementLineDtos = movementLines.stream().map(movementLineMapper::toDto).toList();
        MovementDtoSecond dtoSecond = movementMapper.toDtoSecond(movement);
        dtoSecond.setMovementLines(movementLineDtos);
        return dtoSecond;
    }

    @Override
    public void deleteMovement(UUID id) {
        movementRepository.deleteById(id);
    }

    private List<MovementDto> getList(Page<UUID> idList, Pageable pageable){
        List<Movement> movements = movementRepository.findByIds(idList.getContent(), pageable.getSort());
        return movements.stream().map(movementMapper::toDto).toList();
    }
}
