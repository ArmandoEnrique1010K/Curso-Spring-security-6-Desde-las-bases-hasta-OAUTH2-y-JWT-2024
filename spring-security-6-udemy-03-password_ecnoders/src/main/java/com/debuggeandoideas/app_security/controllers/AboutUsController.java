package com.debuggeandoideas.app_security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

// Indica que esta clase es un controlador de Spring MVC y que los métodos
// devolverán respuestas JSON o XML.
@RestController
// Define la ruta base para este controlador. Todas las solicitudes a
// "/about_us" serán manejadas aquí.
@RequestMapping(path = "/about_us")
public class AboutUsController {

    // Método para manejar solicitudes GET a "/about_us"
    @GetMapping
    public Map<String, String> about() {
        // Lógica de negocio (si fuera necesario) para obtener información "Sobre
        // Nosotros".
        // Devuelve un mapa con un mensaje de respuesta. Este mapa se convierte
        // automáticamente en JSON por @RestController.
        return Collections.singletonMap("msj", "about");
    }
}

// El mismo codigo se repite en los 6 controladores