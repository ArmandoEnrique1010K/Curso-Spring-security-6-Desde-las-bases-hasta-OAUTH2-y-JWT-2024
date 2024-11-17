package com.debuggeandoideas.app_security.components;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Este método es llamado cuando se intenta acceder a una ruta protegida sin
     * autenticación adecuada
     * o con un token JWT inválido.
     * 
     * La implementación de este método envía una respuesta de error con el código
     * HTTP 401 (Unauthorized),
     * indicando que la solicitud no está autorizada debido a la falta de
     * credenciales válidas.
     *
     * @param request       La solicitud HTTP realizada por el cliente.
     * @param response      La respuesta HTTP que será enviada al cliente.
     * @param authException La excepción de autenticación que fue lanzada (si la
     *                      hay).
     * @throws IOException      Si ocurre un error de entrada/salida al escribir la
     *                          respuesta.
     * @throws ServletException Si ocurre un error en el procesamiento del servlet.
     */
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Enviar un error HTTP 401 (Unauthorized) cuando la autenticación falla
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}