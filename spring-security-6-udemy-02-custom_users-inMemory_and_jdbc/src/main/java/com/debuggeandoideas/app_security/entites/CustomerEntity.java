package com.debuggeandoideas.app_security.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

// Indica que esta clase es una entidad JPA mapeada a una tabla de base de datos.
@Entity
// Especifica el nombre de la tabla en la base de datos.
@Table(name = "customers")
// Genera automáticamente métodos como getters, setters, equals, hashCode y
// toString.
@Data
public class CustomerEntity implements Serializable {

    // Define el campo 'id' como clave primaria.
    @Id
    private BigInteger id;

    // Campo para almacenar el correo electrónico del cliente.
    private String email;

    // Mapea el campo 'pwd' de la tabla a la propiedad 'password'.
    @Column(name = "pwd")
    private String password;

    // Mapea el campo 'rol' de la tabla a la propiedad 'role'.
    @Column(name = "rol")
    private String role;
}
