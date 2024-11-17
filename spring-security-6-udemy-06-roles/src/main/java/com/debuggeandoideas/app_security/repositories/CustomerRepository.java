package com.debuggeandoideas.app_security.repositories;

import com.debuggeandoideas.app_security.entites.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;

// Interfaz para acceder a la base de datos usando Spring Data JPA.
// Hereda métodos CRUD básicos de la interfaz CrudRepository.
public interface CustomerRepository extends CrudRepository<CustomerEntity, BigInteger> {

    // Método personalizado para buscar un cliente por su correo electrónico.
    Optional<CustomerEntity> findByEmail(String email);
}