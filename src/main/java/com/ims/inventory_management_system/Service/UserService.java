package com.ims.inventory_management_system.Service;

import java.util.Optional;

import com.ims.inventory_management_system.DTO.RegistrationRequest;
import com.ims.inventory_management_system.Entity.User;

public interface UserService {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    User registerNewUser(RegistrationRequest request, String otp);

    void saveUser(User user);
    
    User findUserByEmailOrThrow(String email);

}
