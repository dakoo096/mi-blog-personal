
package com.proyectoblog.my_personal_blog.service;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//implementamos la seguridad


@Service
public class UserDetailsServiceImpl implements UserDetailsService{ //implementa la interfaz userDetailsService

    @Autowired
    private UserService userService; //inyectamos nuestro service de user
    
    @Autowired
    private HttpSession session; //inyectamos la clase httpsession
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userService.getUserByUsername(username); //nos trae si hay un usuario y cual es
        
        if(optionalUser.isPresent()){//si el optional contiene un valor,va a devolver true,si no tiene nada devuelve un false
            session.setAttribute("user_session_id",optionalUser.get().getId()); //seteamos el valor del atributo session con el id del usuario encontrado
        UserEntity user = optionalUser.get();//guardamos el objeto user que se obtenga en una nueva instacia de user
        return User.builder()//retornamos una clase User que es de spring security //usamos los metodos builder and build para construir nuestro detalle de usuario
        .username(user.getUsername())
        .password(user.getPassword())
        .roles()        
        .build();
        
        }else{
            //en caso de que no exista el usuario tiramos una excepcion
            throw new UsernameNotFoundException("¡User not found!");
        }
    }
    
}
