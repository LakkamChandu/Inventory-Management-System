package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResendOtpRequest {

    @NotEmpty(message = "Email cannot be empty.")
    private String email;
}
