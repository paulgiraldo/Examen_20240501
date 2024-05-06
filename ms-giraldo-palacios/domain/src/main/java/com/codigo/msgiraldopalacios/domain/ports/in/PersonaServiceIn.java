package com.codigo.msgiraldopalacios.domain.ports.in;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.request.PersonaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonaServiceIn {
    ResponseEntity<BaseResponse> crearPersonaIn(PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> buscarXIdIn(Long id);
    ResponseEntity<BaseResponse> buscarTodosIn();
    ResponseEntity<BaseResponse> actualizarIn(Long id, PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> eliminarIn(Long id);

}
