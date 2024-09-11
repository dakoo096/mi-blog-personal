package com.proyectoblog.my_personal_blog.config;

import com.proyectoblog.my_personal_blog.entity.UserEntity;
import com.proyectoblog.my_personal_blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component//esta clase va a implementar una interfaz que se llama handlerInterceptor
public class UserInterceptor implements HandlerInterceptor {

    //hacemos inyeccion de dependencias
    @Autowired
    private UserService userService;

    //si ingresamos a la interfaz handlerInterceptor podemos encontrar este metodo
    @Override //sobreescribimos un metodo que se encuentra en handlerInterceptor
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //creamos una var de tipo httpsession y solicitamos la sesion en tipo falso.
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user_session_id") != null) {//si la session no es null y si nuestro atributo de session no es null
            Long userId = Long.parseLong(session.getAttribute("user_session_id").toString()); //podemos continuar con el proceso para interceptar nuestras solicitudes// debemos realizar un parseo de long de string a long para guardarlo en una variable de tipo long
            Optional<UserEntity> optionalUser = userService.getUserById(userId);//le pasamos nuestro id de nuestro atributo de session.
            if (optionalUser.isPresent()) {////si optionalUser esta presente,voy a poder contianuar con la aplicacion
                request.setAttribute("user", optionalUser.get());//seteamos la clave para nuestro objeto,y el objeto que pasamos en optionalUser.get() ..// establecemos el objeto a los atributos de solicitud con la clave user

            } else {
                return false;
            }

        }
        return true;
    }
}
