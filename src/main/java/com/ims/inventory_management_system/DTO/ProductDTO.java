package com.ims.inventory_management_system.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private Integer quantityInStock;
    private Integer reorderLevel;

}
