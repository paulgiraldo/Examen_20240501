package com.codigo.msgiraldopalacios.domain.ports.out;

import com.codigo.msgiraldopalacios.domain.aggregates.request.PersonaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonaServiceOut {
    ResponseEntity<BaseResponse> crearPersonaOut(PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> buscarXIdOut(Long id);
    ResponseEntity<BaseResponse> buscarTodosOut();
    ResponseEntity<BaseResponse> actualizarOut(Long id, PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> eliminarOut(Long id);

}
