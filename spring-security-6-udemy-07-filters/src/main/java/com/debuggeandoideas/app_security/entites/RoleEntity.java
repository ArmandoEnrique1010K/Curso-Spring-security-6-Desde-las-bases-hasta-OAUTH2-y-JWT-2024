package com.debuggeandoideas.app_security.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity // Indica que esta clase es una entidad JPA que se mapea a una tabla en la base
        // de datos
@Table(name = "roles") // Especifica el nombre de la tabla en la base de datos, en este caso "roles"
@Data // Lombok genera automáticamente los métodos getter y setter, así como otros
      // métodos útiles
public class RoleEntity {

    @Id // Indica que este campo será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor de 'id' se genera automáticamente con un valor
                                                        // incremental
    private BigInteger id; // El identificador único del rol

    @Column(name = "role_name") // Especifica el nombre de la columna en la base de datos
    private String name; // El nombre del rol (por ejemplo, "ADMIN", "USER", etc.)

    private String description; // La descripción del rol (puede ser opcional, dependiendo de la aplicación)

}