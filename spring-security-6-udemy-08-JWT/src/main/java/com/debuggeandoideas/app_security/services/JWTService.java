package com.debuggeandoideas.app_security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

/**
 * Servicio para manejar la generación, validación y extracción de información
 * de los tokens JWT (JSON Web Tokens).
 */
@Service
public class JWTService {

    // Tiempo de validez del token JWT (en segundos): 5 horas.
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Clave secreta para firmar y verificar los tokens JWT.
    public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

    /**
     * Extrae todos los "claims" (reclamaciones) de un token JWT.
     * Los "claims" son el contenido del token, que puede incluir información
     * personalizada.
     *
     * @param token El token JWT del cual se desean extraer los claims.
     * @return Objeto `Claims` que contiene toda la información del token.
     */
    private Claims getAllClaimsFromToken(String token) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae un claim específico del token JWT mediante un resolvedor de funciones.
     *
     * @param token          El token JWT.
     * @param claimsResolver Función para resolver el claim deseado.
     * @param <T>            El tipo de dato del claim que se desea extraer.
     * @return El valor del claim extraído.
     */
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     *
     * @param token El token JWT.
     * @return La fecha de expiración del token.
     */
    private Date getExpirationDateFromToken(String token) {
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    /**
     * Verifica si el token JWT ya ha expirado.
     *
     * @param token El token JWT.
     * @return `true` si el token ha expirado, `false` en caso contrario.
     */
    private Boolean isTokenExpired(String token) {
        final var expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    /**
     * Extrae el nombre de usuario (campo `sub`) del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario contenido en el token.
     */
    public String getUsernameFromToken(String token) {
        return this.getClaimsFromToken(token, Claims::getSubject);
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param userDetails Los detalles del usuario autenticado.
     * @return El token JWT generado.
     */
    public String generateToken(UserDetails userDetails) {
        // Los claims pueden incluir información personalizada, como los roles del
        // usuario.
        final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        return this.getToken(claims, userDetails.getUsername());
    }

    /**
     * Construye y firma un token JWT.
     *
     * @param claims  Información adicional que se incluirá en el token.
     * @param subject El nombre de usuario o identificador principal del token.
     * @return El token JWT generado.
     */
    private String getToken(Map<String, Object> claims, String subject) {
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims) // Agrega los claims al token.
                .setSubject(subject) // Establece el usuario asociado al token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // Fecha de expiración.
                .signWith(key) // Firma el token con la clave secreta.
                .compact(); // Genera el token en formato compacto.
    }

    /**
     * Valida un token JWT comparando el nombre de usuario en el token con el
     * proporcionado
     * por el objeto `UserDetails` y verificando que no haya expirado.
     *
     * @param token       El token JWT a validar.
     * @param userDetails Los detalles del usuario autenticado.
     * @return `true` si el token es válido, `false` en caso contrario.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final var usernameFromUserDetails = userDetails.getUsername();
        final var usernameFromJWT = this.getUsernameFromToken(token);

        return (usernameFromUserDetails.equals(usernameFromJWT)) && !this.isTokenExpired(token);
    }
}
