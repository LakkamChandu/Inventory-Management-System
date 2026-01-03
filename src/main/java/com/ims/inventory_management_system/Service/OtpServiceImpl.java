package com.ims.inventory_management_system.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ims.inventory_management_system.Constants.MessageConstants;
import com.ims.inventory_management_system.DTO.LoginResponse;
import com.ims.inventory_management_system.DTO.OtpVerificationRequest;
import com.ims.inventory_management_system.DTO.ResendOtpRequest;
import com.ims.inventory_management_system.Entity.User;
import com.ims.inventory_management_system.Exception.BadRequestException;
import com.ims.inventory_management_system.Exception.UserAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public String generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Verification code for Inventory Management System");
        message.setText("Hello \n\nYour One-Time Password for registration is "  + otp + "\n\nThis code will expire in 5 minutes. \n\nThank You,\nThe IMS Team");
        mailSender.send(message);
    }

    @Transactional
    public LoginResponse verifyOtp(String email, OtpVerificationRequest request) {
        User user = userService.findUserByEmailOrThrow(email);
        if (user.isEnabled()) {
            throw new UserAlreadyExistsException(MessageConstants.USER_ALREADY_VERIFIED);
        }
        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(MessageConstants.OTP_EXPIRED);
        }
        if (!user.getOtp().equals(request.getOtp())) {
            throw new BadRequestException(MessageConstants.OTP_INVALID);
        }

        user.setEnabled(true);
        user.setOtp(request.getOtp());
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
        userService.saveUser(user);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(token);
    }

    @Transactional
    public String resendOtp(ResendOtpRequest request) {
        User user = userService.findUserByEmailOrThrow(request.getEmail());

        if (user.isEnabled()) {
            throw new UserAlreadyExistsException(MessageConstants.USER_ALREADY_VERIFIED);
        }

        String newOtp = generateOtp();
        user.setOtp(newOtp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
        userService.saveUser(user);
        sendOtpEmail(user.getEmail(), newOtp);

        return MessageConstants.OTP_RESEND_SUCCESS;
    }

}
