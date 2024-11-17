package com.debuggeandoideas.app_security.controllers;

import com.debuggeandoideas.app_security.entites.JWTRequest;
import com.debuggeandoideas.app_security.entites.JWTResponse;
import com.debuggeandoideas.app_security.services.JWTService;
import com.debuggeandoideas.app_security.services.JWTUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor // Genera un constructor con todos los argumentos usando Lombok.
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JWTUserDetailService jwtUserDetailService;
    private final JWTService jwtService;

    /**
     * Maneja solicitudes POST para autenticar usuarios y generar tokens JWT.
     *
     * @param request Objeto que contiene el nombre de usuario y contraseña.
     * @return Respuesta HTTP con el token JWT generado.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
        // Autentica al usuario con las credenciales proporcionadas.
        this.authenticate(request);

        // Carga los detalles del usuario autenticado.
        final var userDetails = this.jwtUserDetailService.loadUserByUsername(request.getUsername());

        // Genera un token JWT basado en los detalles del usuario.
        final var token = this.jwtService.generateToken(userDetails);

        // Retorna el token en el cuerpo de la respuesta.
        return ResponseEntity.ok(new JWTResponse(token));
    }

    /**
     * Método auxiliar para autenticar a un usuario utilizando las credenciales
     * proporcionadas.
     * 
     * @param request Objeto que contiene el nombre de usuario y contraseña.
     */
    private void authenticate(JWTRequest request) {
        try {
            // Intenta autenticar al usuario utilizando el AuthenticationManager.
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException | DisabledException e) {
            // Si la autenticación falla, lanza una excepción con el mensaje
            // correspondiente.
            throw new RuntimeException(e.getMessage());
        }
    }
}
