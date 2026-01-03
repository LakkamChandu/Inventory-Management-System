package com.ims.inventory_management_system.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseOrderItemRequest {

    @NotNull(message = "Product ID should not be null")
    private Long prodId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be atleast 1.")
    private Integer quantity;

    
    @NotNull(message = "Price Per Unit  cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private BigDecimal pricePerUnit;

}
