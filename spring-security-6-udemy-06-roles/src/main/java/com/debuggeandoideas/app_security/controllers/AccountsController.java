package com.debuggeandoideas.app_security.controllers;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {

    // Usamos @PreAuthorize para permitir el acceso solo a usuarios con ciertas
    // autoridades.
    // En este caso, solo los usuarios con cualquiera de las autoridades
    // 'VIEW_ACCOUNT' o 'VIEW_CARDS' pueden acceder.
    @PreAuthorize("hasAnyAuthority('VIEW_ACCOUNT', 'VIEW_CARDS')")
    @GetMapping
    public Map<String, String> accounts() {
        // Lógica de negocio, aquí se puede acceder a la información de las cuentas,
        // dependiendo de la implementación.
        return Collections.singletonMap("msj", "accounts");
    }
}
