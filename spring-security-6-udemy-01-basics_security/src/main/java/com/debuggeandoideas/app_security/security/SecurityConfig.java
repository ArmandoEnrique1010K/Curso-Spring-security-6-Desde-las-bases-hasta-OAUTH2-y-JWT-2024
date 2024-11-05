package com.debuggeandoideas.app_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Indica que esta clase es de configuración y se cargará al inicio de la
// aplicación.
@Configuration
public class SecurityConfig {

    // Define el filtro de seguridad que maneja las reglas de acceso a las rutas de
    // la aplicación
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configura la autorización de solicitudes HTTP
                .authorizeHttpRequests(auth -> auth
                        // Requiere autenticación para las rutas "/loans", "/balance", "/accounts", y
                        // "/cards"
                        .requestMatchers("/loans", "/balance", "/accounts", "/cards").authenticated()
                        // Permite acceso sin autenticación para todas las demás rutas
                        .anyRequest().permitAll())
                // Habilita el inicio de sesión con formulario (formulario de inicio de sesión
                // predeterminado de Spring)
                .formLogin(Customizer.withDefaults())
                // Habilita la autenticación básica HTTP para las solicitudes (útil para APIs)
                .httpBasic(Customizer.withDefaults());

        // Retorna el filtro de seguridad configurado
        return http.build();
    }
}
