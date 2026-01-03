package com.ims.inventory_management_system.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class PurchaseOrderDTO {

    private Long id;
    private Long supplierId;
    private String supplierName;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totalAmount;
    private List<PurchaseOrderItemDTO> orderItems;

}
