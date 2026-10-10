package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.MovementDto;
import com.ravemaster.inventory.domain.dto.MovementDtoSecond;
import com.ravemaster.inventory.domain.request.MovementRequest;
import com.ravemaster.inventory.domain.response.PaginationResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.services.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/movements")
public class MovementController {

    private final MovementService movementService;

    @PostMapping()
    public ResponseEntity<Response> createStockMovement(
            @RequestBody MovementRequest movementRequest){
        MovementDtoSecond movement = movementService.createMovement(movementRequest);
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Stock movement recorded successfully")
                .movementDtoSecond(movement)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping(path = "/get-movement/{id}")
    public ResponseEntity<Response> getStockMovement(
            @PathVariable("id") UUID id
            ){
        MovementDtoSecond movement = movementService.getMovementWithLines(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Stock movement retrieved successfully")
                .movementDtoSecond(movement)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-movement/{id}")
    public ResponseEntity<Response> deleteStockMovement(
            @PathVariable("id") UUID id
    ){
        movementService.deleteMovement(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Stock movement deleted successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/get-movements")
    public ResponseEntity<Response> getAllStockMovements(
            Pageable pageable
    ){
        Page<MovementDto> movement = movementService.getMovements(pageable);
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(movement.getTotalElements())
                .pageSize(movement.getSize())
                .totalPages(movement.getTotalPages())
                .currentPage(movement.getNumber())
                .build();
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Stock movement retrieved successfully")
                .movements(movement.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
