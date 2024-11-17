package com.debuggeandoideas.app_security.security;

import com.debuggeandoideas.app_security.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor // Genera automáticamente un constructor para inicializar los campos finales
                    // marcados en la clase.
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository; // Repositorio para acceder a los datos del usuario en la base
                                                         // de datos.
    private final PasswordEncoder passwordEncoder; // Codificador para verificar contraseñas.

    /**
     * Este método se utiliza para autenticar al usuario con sus credenciales.
     *
     * @param authentication contiene el nombre de usuario y la contraseña.
     * @return un objeto `Authentication` si las credenciales son válidas.
     * @throws AuthenticationException si las credenciales son inválidas.
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Obtiene el nombre de usuario y la contraseña proporcionados por el cliente.
        final var username = authentication.getName(); // Nombre de usuario ingresado.
        final var pwd = authentication.getCredentials().toString(); // Contraseña ingresada.

        // Busca al usuario en la base de datos por su email.
        final var customerFromDb = this.customerRepository.findByEmail(username);

        // Si el usuario no existe, lanza una excepción.
        final var customer = customerFromDb
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials, invalid username"));

        // Obtiene la contraseña almacenada en la base de datos.
        final var customerPwd = customer.getPassword();

        // Verifica si la contraseña ingresada coincide con la almacenada (después de
        // ser codificada).
        if (passwordEncoder.matches(pwd, customerPwd)) {
            // Si las contraseñas coinciden, asigna los roles (authorities) del usuario.
            final var authorities = Collections.singletonList(new SimpleGrantedAuthority(customer.getRole()));

            // Devuelve un token de autenticación con el usuario, la contraseña y las
            // autoridades.
            return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
        } else {
            // Si las contraseñas no coinciden, lanza una excepción.
            throw new BadCredentialsException("Invalid credentials, invalid password");
        }
    }

    /**
     * Indica si este `AuthenticationProvider` puede manejar la clase de
     * autenticación proporcionada.
     *
     * @param authentication la clase de autenticación a verificar.
     * @return true si soporta `UsernamePasswordAuthenticationToken`, false de lo
     *         contrario.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
