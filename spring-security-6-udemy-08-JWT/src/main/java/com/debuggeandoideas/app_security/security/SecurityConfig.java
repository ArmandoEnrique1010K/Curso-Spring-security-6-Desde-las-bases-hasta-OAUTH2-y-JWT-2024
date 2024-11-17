package com.debuggeandoideas.app_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * Define políticas de acceso, autenticación y protección CSRF.
     *
     * @param http                Objeto HttpSecurity para configurar la seguridad.
     * @param jwtValidationFilter Filtro personalizado para validar JWT.
     * @return Cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    @Autowired
    SecurityFilterChain securityFilterChain(HttpSecurity http, JWTValidationFilter jwtValidationFilter)
            throws Exception {

        // Configuración de manejo de sesiones: stateless (sin estado).
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Configuración personalizada para manejar tokens CSRF.
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        // Configuración de autorizaciones basadas en roles.
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/loans", "/balance").hasRole("USER") // Estas rutas requieren el rol "USER".
                .requestMatchers("/accounts", "/cards").hasRole("ADMIN") // Estas rutas requieren el rol "ADMIN".
                .anyRequest().permitAll()) // Permitir todas las demás solicitudes.
                .formLogin(Customizer.withDefaults()) // Habilitar inicio de sesión basado en formularios.
                .httpBasic(Customizer.withDefaults()); // Habilitar autenticación básica HTTP.

        // Agrega el filtro de validación JWT después del filtro de autenticación
        // básica.
        http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);

        // Configuración de CORS (Cross-Origin Resource Sharing).
        http.cors(cors -> corsConfigurationSource());

        // Configuración de protección CSRF.
        http.csrf(csrf -> csrf
                .csrfTokenRequestHandler(requestHandler) // Manejador de tokens CSRF.
                .ignoringRequestMatchers("/welcome", "/about_us", "/authenticate") // Ignorar CSRF en estas rutas.
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // Almacena tokens CSRF en cookies.
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); // Agrega un filtro para
                                                                                          // manejar cookies CSRF.

        return http.build(); // Construye la cadena de filtros de seguridad.
    }

    /**
     * Configura un PasswordEncoder que no realiza ninguna encriptación.
     * Este encoder es útil para pruebas o ejemplos, pero no debe usarse en
     * producción.
     *
     * @return Una instancia de NoOpPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Este encoder no realiza encriptación.
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing) para permitir solicitudes
     * desde diferentes orígenes.
     *
     * @return Fuente de configuración CORS.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        // Permitir solicitudes desde cualquier origen.
        config.setAllowedOrigins(List.of("*"));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.).
        config.setAllowedMethods(List.of("*"));

        // Permitir todos los encabezados en las solicitudes.
        config.setAllowedHeaders(List.of("*"));

        // Registra la configuración CORS para todas las rutas.
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Configura CORS para todas las rutas de la aplicación.

        return source;
    }

    /**
     * Configura el AuthenticationManager, que se utiliza para manejar
     * la autenticación en la aplicación.
     *
     * @param configuration Configuración de autenticación.
     * @return Una instancia de AuthenticationManager.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
