
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //la marcamos como una clase servicio
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;//inyeccion de dependencias
    
    
    @Override
    public void createUser(UserEntity user) {
        userRepository.save(user);
    }
   
    
    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
