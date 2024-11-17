package com.debuggeandoideas.app_security.services;

import com.debuggeandoideas.app_security.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar los detalles de usuario en una autenticación basada
 * en JWT.
 * Implementa la interfaz `UserDetailsService`, que es utilizada por Spring
 * Security
 * para cargar los detalles de un usuario desde un repositorio o base de datos.
 * 
 * Anotaciones:
 * - `@Service`: Marca esta clase como un componente de servicio administrado
 * por Spring.
 * - `@AllArgsConstructor`: Genera automáticamente un constructor que inicializa
 * los campos finales declarados en la clase.
 */
@Service
@AllArgsConstructor
public class JWTUserDetailService implements UserDetailsService {

    // Repositorio para acceder a los datos del usuario en la base de datos.
    private final CustomerRepository customerRepository;

    /**
     * Sobrescribe el método `loadUserByUsername` para cargar los detalles del
     * usuario
     * desde la base de datos utilizando su email.
     * 
     * @param username El nombre de usuario (en este caso, el email) que se desea
     *                 cargar.
     * @return Un objeto `UserDetails` que contiene la información del usuario.
     * @throws UsernameNotFoundException Si no se encuentra el usuario con el email
     *                                   dado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos por su email.
        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    // Convierte los roles del usuario en autoridades de Spring Security.
                    final var authorities = customer.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName())) // Crea un objeto de autoridad por
                                                                                     // cada rol.
                            .toList();

                    // Devuelve un objeto `User` con el email, contraseña y las autoridades del
                    // usuario.
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                })
                // Si no se encuentra el usuario, lanza una excepción indicando que no existe.
                .orElseThrow(() -> new UsernameNotFoundException("User not exist"));
    }
}
