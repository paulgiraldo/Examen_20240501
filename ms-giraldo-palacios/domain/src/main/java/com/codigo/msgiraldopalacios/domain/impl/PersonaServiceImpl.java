package com.codigo.msgiraldopalacios.domain.impl;

import com.codigo.msgiraldopalacios.domain.aggregates.request.PersonaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.in.PersonaServiceIn;
import com.codigo.msgiraldopalacios.domain.ports.out.PersonaServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonaServiceImpl implements PersonaServiceIn {
    private final PersonaServiceOut personaServiceOut;
    @Override
    public ResponseEntity<BaseResponse> crearPersonaIn(PersonaRequest personaRequest) {
        return personaServiceOut.crearPersonaOut(personaRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> buscarXIdIn(Long id) {
        return personaServiceOut.buscarXIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> buscarTodosIn() {
        return personaServiceOut.buscarTodosOut();
    }

    @Override
    public ResponseEntity<BaseResponse> actualizarIn(Long id, PersonaRequest personaRequest) {
        return personaServiceOut.actualizarOut(id, personaRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> eliminarIn(Long id) {
        return personaServiceOut.eliminarOut(id);
    }
}
