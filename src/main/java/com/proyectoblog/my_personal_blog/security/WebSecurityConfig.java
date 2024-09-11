package com.proyectoblog.my_personal_blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//utilizamos spring security para el login y register
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    
    @Bean //indicamos que este metodo va a ser restionado por el contenedor de spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {//recibe como parametro un objeto httpsecurity
        http//usamos el objeto http
                .authorizeHttpRequests((request) -> request//utilizamos el metodo autorizehttprequests con una expresion lamda dentro (pasamos un request como request y lo que sigue es lo que va a retornar
                .requestMatchers("/post/**","/comment/**").authenticated()//mapeamos todas las urls que necesiten autenticacion,todas las solicitudes que entren al endpoint post/ y comment/ van a ser autenticadas
                .anyRequest().permitAll() //cualquier otra solicitud esta permitida para todos los usuarios sin necesidad de autenticarse
                )
                .formLogin((form) -> form //creamos el metodo con una expresion lamda dentro
                .loginPage("/login") //definimos la pagina de login
                .permitAll() //permitimos entrar a todos los usuarios sin necesidad de autenticarse
                .defaultSuccessUrl("/access") //indicamos cual es el endpoint que nos permite acceder dentro de la app o autenticarnos
                )
                .logout((logout) -> logout.permitAll()); //creamos metodo logout para que todos los usuarios tengan permitido deslogearse de nuestra aplicacion
        
        return http.build(); //construimos nuestro filtro de seguridad
    }
    
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){ //este metodo nos retorna una instancia de bcryptpasswordencoder
        return new BCryptPasswordEncoder();//este metodo nos sirve para encriptar nuestra contraseña
    }
}
