package com.ims.inventory_management_system.DTO;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
