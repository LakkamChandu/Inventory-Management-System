package com.ims.inventory_management_system.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdatePurchaseOrderRequest {

    @NotNull(message = "Order date cannot be null.")
    @FutureOrPresent(message = "Order date cannot be in the past.")
    private LocalDate orderDate;

    @Valid 
    @NotEmpty(message = "Purchase order must contain at least one item.")
    private List<PurchaseOrderItemRequest> items;
}
