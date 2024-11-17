package com.debuggeandoideas.app_security.entites;

import lombok.Data;

/**
 * Clase utilizada para representar la solicitud de autenticación con JWT.
 * Contiene el nombre de usuario y la contraseña proporcionados por el cliente.
 * 
 * Anotaciones:
 * - `@Data`: Genera automáticamente métodos como getters, setters, `toString`,
 * `hashCode`, y `equals`, simplificando la definición de clases de datos.
 */
@Data
public class JWTRequest {

    private String username; // Nombre de usuario enviado en la solicitud.
    private String password; // Contraseña enviada en la solicitud.
}
