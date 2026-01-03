package com.ims.inventory_management_system.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PurchaseOrderItemDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
    private BigDecimal pricePerUnit;

}
