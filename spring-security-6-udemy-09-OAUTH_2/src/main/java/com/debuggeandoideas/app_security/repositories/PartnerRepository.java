package com.debuggeandoideas.app_security.repositories;

import com.debuggeandoideas.app_security.entites.PartnerEntity;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface PartnerRepository extends CrudRepository<PartnerEntity, BigInteger> {

    // Este método busca una entidad PartnerEntity utilizando su clientId.
    // Devuelve un Optional, lo que permite manejar el caso en que no se encuentre
    // ningún Partner con el clientId dado.
    Optional<PartnerEntity> findByClientId(String clientId);
}
