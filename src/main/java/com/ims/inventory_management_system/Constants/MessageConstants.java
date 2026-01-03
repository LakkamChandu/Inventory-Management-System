package com.ims.inventory_management_system.Constants;

public class MessageConstants {
    
    //User/Auth Messages
    public static final String REGISTRATION_SUCCESS = "Registration successful! Please check your email (%s) for the OTP to verify your account.";
    public static final String REGISTRATION_NOTE = "Note: OTP will be sent only if your given email is valid!";
    public static final String USERNAME_ALREADY_TAKEN = "Error: Username is already in use!";
    public static final String EMAIL_ALREADY_IN_USE = "Error: Email is already in use!";
    public static final String PASSWORDS_DO_NOT_MATCH = "Error: Passwords do not match!";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with this email: %s";
    public static final String USER_NOT_FOUND_BY_NAME = "User not found with this name : %s";
    public static final String USER_NOT_FOUND_BY_ID = "User not found with this id : %d";
    public static final String USER_ALREADY_VERIFIED = "This user is already verified. Please proceed to login.";
    public static final String OTP_EXPIRED = "OTP has expired. Please request a new one.";
    public static final String OTP_INVALID = "Invalid OTP.";
    public static final String OTP_RESEND_SUCCESS = "A new OTP has been sent to your email address.";
    public static final String USER_NOT_ENABLED = "User account is not yet verified. Please check your email for the OTP.";

    public static final String PRODUCT_NOT_FOUND = "Product not found with this id : %d";

    public static final String PURCHASE_ORDER_RECEIVED = "Purchase Order %d has been successfully RECEIVED. Stock updated and GRN generated: %s";
    public static final String PURCHASE_ORDER_CANCELLED = "Purchase Order %d has been successfully CANCELLED.";

    

}