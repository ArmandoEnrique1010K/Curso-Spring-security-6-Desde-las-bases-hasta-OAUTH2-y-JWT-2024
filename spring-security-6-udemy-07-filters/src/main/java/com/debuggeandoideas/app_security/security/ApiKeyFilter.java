package com.debuggeandoideas.app_security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class ApiKeyFilter extends OncePerRequestFilter {

    // Definimos la clave API esperada. Este valor normalmente estaría en una
    // ubicación más segura, como un archivo de propiedades o un sistema de gestión
    // de secretos.
    private static final String API_KEY = "myKey";

    // Definimos el nombre del encabezado HTTP donde esperamos recibir la clave API.
    private static final String API_KEY_HEADER = "api_key";

    /**
     * Método encargado de filtrar las solicitudes entrantes para verificar la
     * presencia y validez de la clave API.
     * 
     * @param request     La solicitud HTTP que contiene los encabezados.
     * @param response    La respuesta HTTP que será enviada al cliente.
     * @param filterChain La cadena de filtros para continuar el procesamiento de la
     *                    solicitud si la clave API es válida.
     * @throws ServletException Si ocurre algún error en el proceso de filtrado.
     * @throws IOException      Si ocurre un error al leer o escribir en la
     *                          respuesta.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // Intentamos obtener el valor del encabezado "api_key" de la solicitud.
            final var apiKeyOpt = Optional.of(request.getHeader(API_KEY_HEADER));

            // Si no se encuentra el encabezado, lanzamos una excepción
            // BadCredentialsException.
            final var apiKey = apiKeyOpt.orElseThrow(() -> new BadCredentialsException("No header api key"));

            // Comparamos el valor del encabezado con la clave API esperada.
            if (!apiKey.equals(API_KEY)) {
                // Si no coincide la clave, lanzamos una excepción BadCredentialsException con
                // un mensaje adecuado.
                throw new BadCredentialsException("Invalid api key");
            }
        } catch (Exception e) {
            // Si ocurre una excepción durante el proceso (por ejemplo, el encabezado no
            // está presente), lanzamos una BadCredentialsException.
            throw new BadCredentialsException("Invalid api key");
        }

        // Si todo es correcto, se continúa con el procesamiento de la solicitud pasando
        // al siguiente filtro.
        filterChain.doFilter(request, response);
    }
}
