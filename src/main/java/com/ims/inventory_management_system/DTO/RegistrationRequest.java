package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotEmpty(message = "First name cannot be empty.")
    private String firstname;
    
    @NotEmpty(message = "Last name cannot be empty.")
    private String lastname;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;
    
    @NotEmpty(message = "Confirm password cannot be empty.")
    private String confirmPassword;
    
}
