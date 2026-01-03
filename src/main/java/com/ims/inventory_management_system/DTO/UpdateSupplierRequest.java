package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSupplierRequest {

    @Size(max = 100)
    private String contactPerson;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String paymentTerms;

    @Size(max = 255)
    private String website;

    @Size(max = 50)
    private String accountNumber;
}