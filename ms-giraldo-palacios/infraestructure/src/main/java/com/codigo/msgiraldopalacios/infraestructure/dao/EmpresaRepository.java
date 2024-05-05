package com.codigo.msgiraldopalacios.infraestructure.dao;

import com.codigo.msgiraldopalacios.infraestructure.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);

}
