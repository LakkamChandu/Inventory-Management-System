package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSupplierRequest {

    @NotEmpty(message = "Supplier name cannot be Empty")
    private String name;

    
    @Size(max = 100)
    private String contactPerson;

    @NotEmpty(message = "Email cannot be Empty")
    @Email(message = "Please provide a valid Email address")
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

    @NotEmpty(message = "GSTIN cannot be empty")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GSTIN format.")
    @Size(min = 15, max = 15, message = "GSTIN must be 15 characters")
    private String gstin;


}
