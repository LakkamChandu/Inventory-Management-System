package com.ims.inventory_management_system.DTO;

import lombok.Data;

@Data
public class SupplierDTO {

    private Long id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String paymentTerms;
    private String website;
    private String gstin;
    private String status;

}
