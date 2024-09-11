
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import java.util.Optional;

public interface UserService {
    
    void createUser(UserEntity user);
    
    Optional<UserEntity> getUserById(Long id);
    
    Optional<UserEntity> getUserByUsername(String username);
}
