package com.codigo.msgiraldopalacios.infraestructure.mapper;

import com.codigo.msgiraldopalacios.domain.aggregates.dto.EmpresaDto;
import com.codigo.msgiraldopalacios.infraestructure.entity.EmpresaEntity;

import java.sql.Timestamp;

public class EmpresaMapper {

    public static EmpresaDto fromEntity(EmpresaEntity entity){
        EmpresaDto dto = new EmpresaDto();
        dto.setId(entity.getId());
        dto.setRazonSocial(entity.getRazonSocial());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setEstado(entity.getEstado());
        dto.setCondicion(entity.getCondicion());
        dto.setDireccion(entity.getDireccion());
        dto.setDistrito(entity.getDistrito());
        dto.setProvincia(entity.getProvincia());
        dto.setDepartamento(entity.getDepartamento());
        dto.setEsAgenteRetencion(entity.isEsAgenteRetencion());
        dto.setUsuaCrea(entity.getUsuaCrea());
        dto.setDateCreate(entity.getDateCreate());
        dto.setUsuaModif(entity.getUsuaModif());
        dto.setDateModif(entity.getDateModif());
        dto.setUsuaDelet(entity.getUsuaDelet());
        dto.setDateDelet(entity.getDateDelet());
        return dto;
    }

}
