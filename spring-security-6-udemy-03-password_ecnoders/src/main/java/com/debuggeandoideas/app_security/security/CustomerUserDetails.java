package com.debuggeandoideas.app_security.security;

import com.debuggeandoideas.app_security.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Indica que esta clase es un servicio de Spring y se puede inyectar en otras partes de la aplicación.
@Service
// Marca los métodos de esta clase como transaccionales.
@Transactional
// Genera un constructor con todos los campos finales (final).
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService {

    // Repositorio para acceder a los datos del cliente desde la base de datos.
    private final CustomerRepository customerRepository;

    // Sobrescribe el método para cargar detalles del usuario por su nombre de
    // usuario (correo electrónico).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el cliente en el repositorio usando su correo electrónico.
        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    // Crea una lista de roles (authorities) asignados al usuario.
                    var authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
                    // Retorna un objeto User con el correo, contraseña y roles.
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                })
                // Lanza una excepción si el usuario no se encuentra.
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}