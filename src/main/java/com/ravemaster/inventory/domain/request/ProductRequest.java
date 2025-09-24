package com.ravemaster.inventory.domain.request;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Stock quantity cannot be null")
    private Integer stockQuantity;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;

    @NotNull(message = "Unit price cannot be null")
    private Double unitPrice;

}
