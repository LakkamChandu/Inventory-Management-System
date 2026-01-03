package com.ims.inventory_management_system.DTO;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePurchaseOrderRequest {

     @NotNull(message = "Supplier ID cannot be null.")
    private Long supplierId;

    @NotNull(message = "Order date cannot be null.")
    @FutureOrPresent(message = "Order date cannot be in the past.")
    private LocalDate orderDate;

    @Valid   //Validate the objects inside the list
    @NotEmpty(message = "Purchase order must contain at least one item.")
    private List<PurchaseOrderItemRequest> items;
}
