package com.ims.inventory_management_system.Controller;

import com.ims.inventory_management_system.DTO.ApiResponse;
import com.ims.inventory_management_system.DTO.LoginRequest;
import com.ims.inventory_management_system.DTO.LoginResponse;
import com.ims.inventory_management_system.DTO.OtpVerificationRequest;
import com.ims.inventory_management_system.DTO.RegistrationRequest;
import com.ims.inventory_management_system.DTO.ResendOtpRequest;
import com.ims.inventory_management_system.Service.AuthServiceImpl;
import com.ims.inventory_management_system.Service.OtpServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final OtpServiceImpl otpService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        ApiResponse response = authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(@Valid @RequestHeader("User-Email") String email, @RequestBody OtpVerificationRequest request) {
        LoginResponse response = otpService.verifyOtp(email,request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        String message = otpService.resendOtp(request);
        return ResponseEntity.ok(new ApiResponse(message, true));
    }
}
