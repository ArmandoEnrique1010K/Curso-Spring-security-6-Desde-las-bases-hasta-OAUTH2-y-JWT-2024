package com.debuggeandoideas.app_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

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

    /*
     * Configuración para gestionar usuarios en memoria.
     * Esta parte esta comentada porque se esta usando un repositorio que llama a la
     * base de datos.
     */
    /*
     * @Bean
     * InMemoryUserDetailsManager inMemoryUserDetailsManager() {
     * var admin = User.withUsername("admin")
     * .password("to_be_encoded")
     * .authorities("ADMIN")
     * .build();
     * 
     * var user = User.withUsername("user")
     * .password("to_be_encoded")
     * .authorities("USER")
     * .build();
     * 
     * return new InMemoryUserDetailsManager(admin, user);
     * }
     */

    /*
     * Configuración para gestionar usuarios desde una base de datos.
     */
    /*
     * @Bean
     * UserDetailsService userDetailsService(DataSource dataSource) {
     * return new JdbcUserDetailsManager(dataSource);
     * }
     */

    // Define el codificador de contraseñas. En este caso, usa un codificador que no
    // realiza ningún cifrado (NoOp).
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
