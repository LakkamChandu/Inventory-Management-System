package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductRequest {

    @NotEmpty(message = "SKU cannot be empty.")
    @Size(max = 50,message = "SKU cannot exceed 50 characters.")
    private String sku;

    @NotEmpty(message = "Name cannot be Empty.")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500,message = "Description Cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than Zero")
    private Double price;

    @Min(value = 0, message = "Initial quantity cannot be negative.")
    private Integer quantityInStock = 0;

}
