package com.ravemaster.inventory.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ravemaster.inventory.domain.dto.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    //Generic
    private int status;
    private String message;

    //for login
    private AuthResponse authResponse;

    //for pagination
    private PaginationResponse paginationResponse;

    //data output optionals
    private UserDto user;
    private List<UserDto> users;

    private CategoryDto category;
    private List<CategoryDto> categories;

    private ProductDto product;
    private List<ProductDto> products;

    private TransactionDto transactionDto;
    private TransactionDtoSecond transactionDtoSecond;
    private List<TransactionDto> transactions;

    //for errors
    private ApiErrorResponse apiErrorResponse;
}
