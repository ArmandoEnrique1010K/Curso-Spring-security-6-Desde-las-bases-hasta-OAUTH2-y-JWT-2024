package com.debuggeandoideas.app_security.security;

import com.debuggeandoideas.app_security.services.CustomerUserDetails;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    // Configuración del filtro de seguridad para el servidor de autorización OAuth2
    @Bean
    @Order(1)
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        // Aplica la configuración predeterminada del servidor de autorización OAuth2
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // Habilita OpenID Connect (OIDC)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        // Configuración para manejar excepciones de autenticación
        http.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
        return http.build();
    }

    // Configuración del filtro de seguridad para la aplicación cliente
    @Bean
    @Order(2)
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de login con formulario
        http.formLogin(Customizer.withDefaults());

        // Autorizaciones específicas según roles
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN) // Solo accesible para ADMIN
                .requestMatchers(USER_RESOURCES).hasRole(ROLE_USER) // Solo accesible para USER
                .anyRequest().permitAll()); // Otros recursos son públicos

        // Configuración del servidor de recursos OAuth2 con validación JWT
        http.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // Bean para codificar contraseñas usando BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Proveedor de autenticación que usa detalles de usuario y codificación de
    // contraseñas
    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(encoder);
        authProvider.setUserDetailsService(userDetails);
        return authProvider;
    }

    // Configuración del servidor de autorización
    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    // Conversor de autenticación para extraer roles de JWT
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authConverter = new JwtGrantedAuthoritiesConverter();
        authConverter.setAuthoritiesClaimName("roles");
        authConverter.setAuthorityPrefix(""); // No añade prefijos a las autoridades
        var converterResponse = new JwtAuthenticationConverter();
        converterResponse.setJwtGrantedAuthoritiesConverter(authConverter);
        return converterResponse;
    }

    // Fuente de claves JWK para firmar tokens
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        var rsa = generateKeys();
        var jwkSet = new JWKSet(rsa);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    // Decodificador de JWT usando la fuente de claves JWK
    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // Personalizador de tokens OAuth2 para agregar información adicional al token
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            var authentication = context.getPrincipal();
            var authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claims(claim -> claim.putAll(Map.of(
                        "roles", authorities, // Incluye roles en el token
                        "owner", APPLICATION_OWNER, // Propietario de la aplicación
                        "date_request", LocalDateTime.now().toString()))); // Fecha de la solicitud
            }
        };
    }

    // Genera un par de claves RSA
    private static KeyPair generateRSA() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(RSA_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    // Genera claves JWK basadas en RSA
    private static RSAKey generateKeys() {
        var keyPair = generateRSA();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString()) // Identificador único para la clave
                .build();
    }

    // Recursos protegidos para usuarios
    private static final String[] USER_RESOURCES = { "/loans/**", "/balance/**" };
    // Recursos protegidos para administradores
    private static final String[] ADMIN_RESOURCES = { "/accounts/**", "/cards/**" };
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final String LOGIN_RESOURCE = "/login"; // Página de login
    private static final String RSA = "RSA";
    private static final Integer RSA_SIZE = 2048; // Tamaño de la clave RSA
    private static final String APPLICATION_OWNER = "Debuggeando ideas"; // Nombre de la aplicación
}
