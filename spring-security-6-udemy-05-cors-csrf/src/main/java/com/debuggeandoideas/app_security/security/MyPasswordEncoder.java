package com.debuggeandoideas.app_security.security;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Componente personalizado para codificar y verificar contraseñas.
// Está comentado y no está siendo utilizado en la configuración actual,
// pero puede servir como referencia para personalizaciones futuras.
// @Component indica que Spring debe gestionar esta clase como un componente.
// @Component
public class MyPasswordEncoder /* implements PasswordEncoder */ {

    // Codifica una contraseña en texto plano utilizando una función hash simple.
    // Este método convierte la contraseña en su representación hash usando
    // `hashCode()`.
    public String encode(CharSequence rawPassword) {
        // Calcula el hash de la contraseña en texto plano.
        return String.valueOf(rawPassword.toString().hashCode());
    }

    // Verifica si una contraseña en texto plano coincide con su versión codificada.
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Convierte la contraseña en texto plano a su representación hash.
        var passwordAsString = String.valueOf(rawPassword.toString().hashCode());
        // Compara la contraseña codificada proporcionada con la calculada.
        return encodedPassword.equals(passwordAsString);
    }
}