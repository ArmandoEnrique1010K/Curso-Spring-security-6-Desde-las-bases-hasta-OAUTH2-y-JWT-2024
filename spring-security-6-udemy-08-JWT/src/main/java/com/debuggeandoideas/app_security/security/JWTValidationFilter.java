package com.debuggeandoideas.app_security.security;

import com.debuggeandoideas.app_security.services.JWTService;
import com.debuggeandoideas.app_security.services.JWTUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Filtro personalizado para validar tokens JWT en cada solicitud.
 * Este filtro se ejecuta una vez por petición y se asegura de que el token
 * JWT en el encabezado de autorización sea válido.
 */
@Component
@AllArgsConstructor
@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    // Servicios necesarios para la validación del JWT y la obtención de detalles
    // del usuario.
    private final JWTService jwtService;
    private final JWTUserDetailService jwtUserDetailService;

    // Constantes para manejar el encabezado de autorización.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    /**
     * Método principal del filtro que valida el token JWT y establece
     * la autenticación en el contexto de seguridad.
     *
     * @param request     Objeto de solicitud HTTP.
     * @param response    Objeto de respuesta HTTP.
     * @param filterChain Cadena de filtros a seguir.
     * @throws ServletException Excepción lanzada por el servlet.
     * @throws IOException      Excepción de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Extrae el encabezado de autorización de la solicitud.
        final var requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        String username = null;
        String jwt = null;

        // Verifica si el encabezado tiene un token y si comienza con "Bearer ".
        if (Objects.nonNull(requestTokenHeader)
                && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {

            // Extrae el token eliminando el prefijo "Bearer ".
            jwt = requestTokenHeader.substring(7);

            try {
                // Obtiene el nombre de usuario (subject) del token JWT.
                username = jwtService.getUsernameFromToken(jwt);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage()); // Maneja errores en la extracción del token.
            } catch (ExpiredJwtException e) {
                log.warn(e.getMessage()); // Maneja casos donde el token ha expirado.
            }
        }

        // Si se obtiene un nombre de usuario y no hay una autenticación ya configurada.
        if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {

            // Carga los detalles del usuario desde el servicio.
            final var userDetails = this.jwtUserDetailService.loadUserByUsername(username);

            // Valida el token con los detalles del usuario.
            if (this.jwtService.validateToken(jwt, userDetails)) {

                // Crea un token de autenticación utilizando los detalles del usuario.
                var usernameAndPassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Establece detalles adicionales relacionados con la solicitud.
                usernameAndPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad.
                SecurityContextHolder.getContext().setAuthentication(usernameAndPassAuthToken);
            }
        }

        // Continúa con la cadena de filtros.
        filterChain.doFilter(request, response);
    }
}
