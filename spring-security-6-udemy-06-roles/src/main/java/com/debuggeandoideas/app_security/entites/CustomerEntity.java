package com.debuggeandoideas.app_security.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Entity // Indica que esta clase es una entidad JPA que se mapea a una tabla en la base
        // de datos
@Table(name = "customers") // Especifica el nombre de la tabla que se utilizará en la base de datos
@Data // Lombok genera automáticamente los métodos getter y setter, así como otros
      // métodos útiles
public class CustomerEntity implements Serializable {

    @Id // Indica que este campo será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el valor de 'id' se genera automáticamente con un
                                                        // valor incremental
    private BigInteger id; // El identificador único del cliente

    private String email; // El email del cliente. Este campo se mapea a una columna de la tabla

    @Column(name = "pwd") // La columna de la base de datos tiene el nombre "pwd"
    private String password; // La contraseña del cliente, almacenada como texto

    @OneToMany(fetch = FetchType.EAGER) // Relación uno a muchos, un cliente puede tener múltiples roles
    @JoinColumn(name = "id_customer") // Indica la columna de la tabla 'roles' que se usa para establecer la relación
                                      // con la tabla 'customers'
    private List<RoleEntity> roles; // Lista de roles asignados al cliente, mapeada a la tabla 'roles'

}
