package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {

    @NotEmpty(message = "Product name cannot be empty.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotNull(message = "Price cannot be empty.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero.")
    private Double price;

}
