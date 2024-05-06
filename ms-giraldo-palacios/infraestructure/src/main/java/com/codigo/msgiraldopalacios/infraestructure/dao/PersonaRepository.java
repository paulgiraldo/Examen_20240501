package com.codigo.msgiraldopalacios.infraestructure.dao;

import com.codigo.msgiraldopalacios.infraestructure.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
    boolean existsByNumeroDocumento(String numeroDocumento);
    boolean existsByEmail(String email);
}
