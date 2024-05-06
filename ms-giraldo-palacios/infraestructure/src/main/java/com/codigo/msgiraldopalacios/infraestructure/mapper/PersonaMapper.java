package com.codigo.msgiraldopalacios.infraestructure.mapper;

import com.codigo.msgiraldopalacios.domain.aggregates.dto.EmpresaDto;
import com.codigo.msgiraldopalacios.domain.aggregates.dto.PersonaDto;
import com.codigo.msgiraldopalacios.infraestructure.entity.EmpresaEntity;
import com.codigo.msgiraldopalacios.infraestructure.entity.PersonaEntity;

import java.sql.Timestamp;

public class PersonaMapper {
    public static PersonaDto fromEntity(PersonaEntity entity) {
        PersonaDto dto = new PersonaDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setEstado(entity.getEstado());
        dto.setUsuaCrea(entity.getUsuaCrea());
        dto.setDateCreate(entity.getDateCreate());
        dto.setUsuaModif(entity.getUsuaModif());
        dto.setDateModif(entity.getDateModif());
        dto.setUsuaDelet(entity.getUsuaDelet());
        dto.setDateDelet(entity.getDateDelet());
        dto.setEmpresa_id(entity.getEmpresa_id());
        return dto;
    }
}