package com.debuggeandoideas.app_security.entites;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase utilizada para encapsular la respuesta de autenticación JWT.
 * Contiene el token JWT generado después de una autenticación exitosa.
 * 
 * Anotaciones:
 * - `@Data`: Genera automáticamente métodos como getters, setters, `toString`,
 * `hashCode`, y `equals`.
 * - `@AllArgsConstructor`: Genera automáticamente un constructor que inicializa
 * todos los campos de la clase.
 */
@Data
@AllArgsConstructor
public class JWTResponse {

    private String jwt; // Token JWT generado después de una autenticación exitosa.
}
