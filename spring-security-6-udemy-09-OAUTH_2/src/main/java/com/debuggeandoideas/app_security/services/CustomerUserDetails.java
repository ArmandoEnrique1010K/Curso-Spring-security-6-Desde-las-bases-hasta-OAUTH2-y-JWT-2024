package com.debuggeandoideas.app_security.services;

import com.debuggeandoideas.app_security.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /**
     * Carga un usuario desde la base de datos basado en su nombre de usuario
     * (correo electrónico).
     *
     * @param username el nombre de usuario del cliente (en este caso, el correo
     *                 electrónico).
     * @return un objeto UserDetails que contiene las credenciales y roles del
     *         usuario.
     * @throws UsernameNotFoundException si no se encuentra el usuario en la base de
     *                                   datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.customerRepository.findByEmail(username)
                .map(customer -> {
                    // Obtiene los roles asociados al usuario
                    final var roles = customer.getRoles();

                    // Convierte los roles en una lista de SimpleGrantedAuthority
                    final var authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList());

                    // Crea un objeto User con email, contraseña y roles
                    return new User(customer.getEmail(), customer.getPassword(), authorities);
                }).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
