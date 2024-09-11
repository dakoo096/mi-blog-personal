
package com.proyectoblog.my_personal_blog.Controller;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")//mapeamos a la ruta raiz
public class UserController {
    
    @Autowired//importamos nuestro servicio
    private UserService userService;
    
    @Autowired //inyectamos la clase para autenticar
    private BCryptPasswordEncoder passwordEncoder;
    
    //creamos nuestros endpoint para mapear nuestras vistas de login como registrarse
    
    
    //vista registro
    @GetMapping("/record")
    public String recordPage(){
        return "/users/register";//nos va retornar la vista registrar
    }
    
    //endpoint que nos permite registrarnos
    @PostMapping("/register")
    public String register(UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));//seteamos la contraseña y la encriptamos
        userService.createUser(user);//registramos
        return "redirect:/login";//retorna al endpoint del login
    }
    
    @GetMapping(value ={"/login","/"})//si las mapeamos con vaue y dentro de llaves podemos hacer que mas de una vista muestren lo mismo
    public String loginPage(){
        return "users/login";
    }
    
    
    @GetMapping("/access")
    public String access(HttpSession session){
        //traemos nuestra entidad user ,que va a ser igual a nuestro servicio ,trayendo la id,usaremos nuestra variable de sesion que seteamos en userDetailsServiceImpl,le hacemos un parseo a long y lo convertimos en un string
        Optional<UserEntity> optionalUser = userService.getUserById(Long.valueOf(session.getAttribute("user_session_id").toString()));
     
        if(optionalUser.isPresent()){//en caso de que optionalUser encuentre un valor
         session.setAttribute("user_session_id",optionalUser.get().getId());//seteamos el atributo de sesion con el nuevo valor del usuario que se acaba de logear
         return "redirect:post/home";//hacemos el redirect a home
        
        }   else{
         return "redirect:/login";//si devuelve valor en falso devuelve al login
     }
    }
    
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false); //creamos una variable de tipo httpsession 
        
        if(session != null){ //si la sesion es diferente de null
            session.invalidate(); //invalida la sesion
        }
        return "redirect:/login"; //retorna a login
    }
    
    
    
}
