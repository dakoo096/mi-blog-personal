package com.proyectoblog.my_personal_blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    /**
     * Redirecciona las rutas del navegador que no corresponden a archivos estáticos ni a /api
     * hacia el index.html de Vue para soportar el enrutamiento del lado del cliente (SPA).
     */
    @GetMapping(value = "/{path:[^\\.]*}")
    public String forwardSpa() {
        return "forward:/index.html";
    }
}
