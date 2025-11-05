package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.TransactionDto;
import com.ravemaster.inventory.domain.dto.TransactionDtoSecond;
import com.ravemaster.inventory.domain.request.TransactionRequest;
import com.ravemaster.inventory.domain.response.PaginationResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.services.TransactionService;
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
@RequestMapping(path = "api/v1/transactions")
public class TransactionController{

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Response> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest
    ){
        TransactionDto transactionDto = transactionService.createTransaction(transactionRequest);
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Success")
                .transactionDto(transactionDto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> getTransactionWithLines(
            @PathVariable UUID id
    ){
        TransactionDtoSecond transaction = transactionService.getTransactionWithLines(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .transactionDtoSecond(transaction)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Response> deleteTransaction(
            @PathVariable UUID id
    ){
        transactionService.deleteTransaction(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllTransactions(Pageable pageable){
        Page<TransactionDto> transactions = transactionService.getTransactions(pageable);
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .currentPage(transactions.getNumber())
                .pageSize(transactions.getSize())
                .build();
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .transactions(transactions.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/transaction-type")
    public ResponseEntity<Response> getAllTransactionsByType(@RequestParam("transactionType") String transactionType,Pageable pageable){
        Page<TransactionDto> transactions = transactionService.getTransactionsByType(pageable, transactionType);
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .currentPage(transactions.getNumber())
                .pageSize(transactions.getSize())
                .build();
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .transactions(transactions.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/sale-type")
    public ResponseEntity<Response> getAllTransactionsBySale(@RequestParam("saleType") String saleType,Pageable pageable){
        Page<TransactionDto> transactions = transactionService.getTransactionsBySale(pageable, saleType);
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .currentPage(transactions.getNumber())
                .pageSize(transactions.getSize())
                .build();
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .transactions(transactions.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
