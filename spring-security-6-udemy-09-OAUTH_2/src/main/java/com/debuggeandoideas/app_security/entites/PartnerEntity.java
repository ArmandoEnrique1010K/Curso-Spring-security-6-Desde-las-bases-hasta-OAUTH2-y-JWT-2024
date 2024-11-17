package com.debuggeandoideas.app_security.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity // Indica que esta clase es una entidad JPA que se mapeará a una tabla en la
        // base de datos.
@Table(name = "partners") // Mapea la entidad a la tabla 'partners' en la base de datos.
@Data // Lombok: genera automáticamente getters, setters, equals(), hashCode() y
      // toString() para todos los campos.
public class PartnerEntity {

    @Id // Indica que este campo es la clave primaria de la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que el valor del campo 'id' se generará
                                                        // automáticamente.
    private BigInteger id;

    private String clientId; // Identificador único del cliente de OAuth. Usado para identificar el cliente
                             // en el flujo de autorización.

    private String clientName; // Nombre del cliente que está utilizando OAuth para acceder a los recursos.

    private String clientSecret; // Secreto asociado al cliente OAuth. Este valor es importante para verificar
                                 // que las solicitudes provienen de un cliente autorizado.

    private String scopes; // Los permisos o alcances (scopes) asociados al cliente OAuth. Los scopes
                           // definen los recursos a los que el cliente tiene acceso.

    private String grantTypes; // Define los tipos de concesión (grant types) que el cliente puede usar en
                               // OAuth, como 'authorization_code', 'client_credentials', etc.

    private String authenticationMethods; // Métodos de autenticación que se utilizarán para verificar la identidad del
                                          // cliente.

    private String redirectUri; // URI de redirección a la que se envía al usuario después de que se complete el
                                // flujo de autorización.

    private String redirectUriLogout; // URI de redirección después del logout del cliente o la sesión de
                                      // autorización.
}