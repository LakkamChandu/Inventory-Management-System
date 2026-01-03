package com.ims.inventory_management_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {

    private String message;
    private UserDTO user;
    private String token;

}
