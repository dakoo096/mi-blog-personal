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
        if (session != null && session.getAttribute("user_session_id") != null) {
            Long userId = Long.parseLong(session.getAttribute("user_session_id").toString());
            Optional<UserEntity> optionalUser = userService.getUserById(userId);
            if (optionalUser.isPresent()) {
                request.setAttribute("user", optionalUser.get());
            }
        }
        return true;
    }
}
