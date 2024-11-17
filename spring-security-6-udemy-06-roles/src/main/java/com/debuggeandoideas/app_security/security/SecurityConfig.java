package com.debuggeandoideas.app_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
// @EnableMethodSecurity // Esta anotación se utiliza para habilitar la
// seguridad a nivel de métodos,
// permitiendo configurar la autorización de acceso a métodos específicos. Está
// comentada por ahora.
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Creamos un manejador para gestionar el token CSRF en las solicitudes.
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf"); // Nombre del atributo para el token CSRF

        // Configura la autorización de acceso a las rutas.
        http.authorizeHttpRequests(auth ->
        // http.authorizeHttpRequests(auth -> auth.requestMatchers("/loans", "/balance",
        // "/accounts", "/cards") // Estas rutas requieren autenticación

        // La siguiente configuración define los permisos de acceso para las rutas de la
        // aplicación.
        auth
                // Las rutas /loans y /balance requieren que el usuario tenga el rol "USER".
                .requestMatchers("/loans", "/balance").hasRole("USER")
                // Las rutas /accounts y /cards requieren que el usuario tenga el rol "ADMIN".
                .requestMatchers("/accounts", "/cards").hasRole("ADMIN")
                // Puedes personalizar aún más la autorización basándote en permisos específicos
                // con hasAuthority,
                // por ejemplo, .requestMatchers("/loans").hasAuthority("VIEW_LOANS").
                // .requestMatchers("/balance").hasAuthority("VIEW_BALANCE")
                // .requestMatchers("/cards").hasAuthority("VIEW_CARDS")
                // .requestMatchers("/accounts").hasAnyAuthority("VIEW_ACCOUNT", "VIEW_CARDS")

                .anyRequest().permitAll()) // Cualquier otra ruta está permitida sin autenticación
                .formLogin(Customizer.withDefaults()) // Configuración predeterminada para el formulario de login
                .httpBasic(Customizer.withDefaults()); // Configuración predeterminada para autenticación básica

        // Configuración de CORS (Cross-Origin Resource Sharing).
        http.cors(cors -> corsConfigurationSource()); // Se utiliza el método corsConfigurationSource() para configurar
                                                      // CORS

        // Configuración de CSRF (Cross-Site Request Forgery).
        http.csrf(csrf -> csrf
                .csrfTokenRequestHandler(requestHandler) // Usa el manejador para gestionar el token CSRF
                .ignoringRequestMatchers("/welcome", "/about_us") // Rutas que no requieren protección CSRF
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // Almacena el token CSRF en una
                                                                                     // cookie y la hace accesible desde
                                                                                     // JavaScript
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); // Se agrega el filtro
                                                                                          // personalizado de CSRF
                                                                                          // después del filtro de
                                                                                          // autenticación básica
        return http.build(); // Construye la configuración de seguridad
    }

    // Configura un PasswordEncoder que no realiza ninguna encriptación
    // (NoOpPasswordEncoder).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Este PasswordEncoder no encripta las contraseñas, es solo para
                                                  // pruebas o ejemplos.
    }

    // Configura CORS (Cross-Origin Resource Sharing) para permitir solicitudes
    // desde diferentes orígenes.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        // Permite solicitudes desde cualquier origen (esto se puede restringir a
        // dominios específicos)
        config.setAllowedOrigins(List.of("*"));

        // Permite todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(List.of("*"));

        // Permite todos los encabezados en las solicitudes
        config.setAllowedHeaders(List.of("*"));

        // Registra la configuración CORS para todas las rutas.
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Configura CORS para todas las rutas de la aplicación

        return source; // Devuelve la configuración CORS
    }

}