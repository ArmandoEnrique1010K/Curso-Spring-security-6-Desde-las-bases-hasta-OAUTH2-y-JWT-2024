package com.debuggeandoideas.app_security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class CsrfCookieFilter extends OncePerRequestFilter {

    // Este método se ejecuta para cada solicitud HTTP.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Recupera el token CSRF asociado con la solicitud. El token se almacena como
        // un atributo en la solicitud.
        var csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Si el token CSRF existe y tiene un nombre de encabezado válido
        if (Objects.nonNull(csrfToken.getHeaderName())) {
            // Se agrega el token CSRF como un encabezado en la respuesta HTTP para que
            // pueda ser utilizado por el cliente
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }

        // Continúa con la cadena de filtros, permitiendo que la solicitud pase al
        // siguiente filtro o a la lógica de la aplicación
        filterChain.doFilter(request, response);
    }
}