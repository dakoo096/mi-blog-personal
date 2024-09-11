package com.proyectoblog.my_personal_blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer { //implementa la interfaz webmvcconfigurer
    
    
    @Autowired 
    private UserInterceptor userInterceptor;
    
    @Override //copiamos el metodo desde webmvcconfig y sobreescribimos con override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        /* 0: return
         *  */
        // </editor-fold>
    }
    
    
}
