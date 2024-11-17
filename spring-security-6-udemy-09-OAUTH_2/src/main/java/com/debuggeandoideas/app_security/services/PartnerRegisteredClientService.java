package com.debuggeandoideas.app_security.services;

import com.debuggeandoideas.app_security.repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class PartnerRegisteredClientService implements RegisteredClientRepository {

    // Repositorio para interactuar con la base de datos y obtener información de
    // los partners (clientes registrados)
    private PartnerRepository partnerRepository;

    /**
     * Busca un cliente registrado por su clientId.
     * 
     * @param clientId El identificador del cliente que se busca.
     * @return Un objeto RegisteredClient que representa al cliente registrado.
     * @throws BadCredentialsException si el cliente no existe en la base de datos.
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {
        var partnerOpt = this.partnerRepository.findByClientId(clientId);

        // Si el cliente existe, mapea los datos a un RegisteredClient
        return partnerOpt.map(partner -> {
            // Convierte las grant types de la base de datos a objetos
            // AuthorizationGrantType
            var authorizationGrantTypes = Arrays.stream(partner.getGrantTypes().split(","))
                    .map(AuthorizationGrantType::new)
                    .toList();

            // Convierte los métodos de autenticación a objetos ClientAuthenticationMethod
            var clientAuthenticationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
                    .map(ClientAuthenticationMethod::new)
                    .toList();

            // Convierte los scopes de la base de datos a una lista de strings
            var scopes = Arrays.stream(partner.getScopes().split(",")).toList();

            // Construye y retorna el RegisteredClient con la información obtenida
            return RegisteredClient
                    .withId(partner.getId().toString()) // ID del cliente
                    .clientId(partner.getClientId()) // Client ID
                    .clientSecret(partner.getClientSecret()) // Secret del cliente
                    .clientName(partner.getClientName()) // Nombre del cliente
                    .redirectUri(partner.getRedirectUri()) // URI de redirección
                    .postLogoutRedirectUri(partner.getRedirectUriLogout()) // URI de redirección tras logout
                    .clientAuthenticationMethod(clientAuthenticationMethods.get(0)) // Primer método de autenticación
                    .clientAuthenticationMethod(clientAuthenticationMethods.get(1)) // Segundo método de autenticación
                    .scope(scopes.get(0)) // Primer scope
                    .scope(scopes.get(1)) // Segundo scope
                    .authorizationGrantType(authorizationGrantTypes.get(0)) // Primer grant type
                    .authorizationGrantType(authorizationGrantTypes.get(1)) // Segundo grant type
                    .tokenSettings(this.tokenSettings()) // Configuración de token
                    .build();
        }).orElseThrow(() -> new BadCredentialsException("Client not exist")); // Lanza una excepción si el cliente no
                                                                               // existe
    }

    /**
     * Guarda un RegisteredClient. No implementado en esta clase.
     *
     * @param registeredClient El cliente que se desea guardar.
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        // Este método no está implementado
    }

    /**
     * Busca un RegisteredClient por su ID. No implementado en esta clase.
     *
     * @param id El ID del cliente que se busca.
     * @return Siempre retorna null, ya que este método no está implementado.
     */
    @Override
    public RegisteredClient findById(String id) {
        return null; // Este método no está implementado
    }

    /**
     * Configuración de los tokens emitidos para los clientes registrados.
     *
     * @return Un objeto TokenSettings que define la configuración del token (ej.
     *         tiempo de vida).
     */
    private TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(8)) // Define un tiempo de vida de 8 horas para los tokens de
                                                            // acceso
                .build();
    }
}
