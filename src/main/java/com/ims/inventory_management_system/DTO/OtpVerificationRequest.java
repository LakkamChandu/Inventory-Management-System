package com.ims.inventory_management_system.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OtpVerificationRequest {

    @NotEmpty(message = "OTP cannot be empty.")
    private String otp;
    
}
