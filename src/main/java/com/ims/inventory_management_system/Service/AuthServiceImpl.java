package com.ims.inventory_management_system.Service;

import org.springframework.stereotype.Service;

import com.ims.inventory_management_system.Constants.MessageConstants;
import com.ims.inventory_management_system.DTO.ApiResponse;
import com.ims.inventory_management_system.DTO.LoginRequest;
import com.ims.inventory_management_system.DTO.LoginResponse;
import com.ims.inventory_management_system.DTO.RegistrationRequest;
import com.ims.inventory_management_system.Entity.User;
import com.ims.inventory_management_system.Exception.IllegalArgumentException;
import com.ims.inventory_management_system.Exception.UserAlreadyExistsException;
import com.ims.inventory_management_system.Repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final OtpServiceImpl otpService;

    public ApiResponse registerUser(RegistrationRequest request){

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException(MessageConstants.PASSWORDS_DO_NOT_MATCH);
        }

        if (userService.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(MessageConstants.USERNAME_ALREADY_TAKEN);
        }

        //Checks if user already registered
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException(MessageConstants.EMAIL_ALREADY_IN_USE);
        }

        String otp = otpService.generateOtp();
        User savedUser = userService.registerNewUser(request, otp);
        
        otpService.sendOtpEmail(savedUser.getEmail(), otp);

        String message = String.format(MessageConstants.REGISTRATION_SUCCESS, savedUser.getEmail());
        return new ApiResponse(message + MessageConstants.REGISTRATION_NOTE,true);
    }


    public LoginResponse login(LoginRequest loginrequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginrequest.getEmail(),
                loginrequest.getPassword())
                );
        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userdetails);
        return new LoginResponse(token);
    
    }
}