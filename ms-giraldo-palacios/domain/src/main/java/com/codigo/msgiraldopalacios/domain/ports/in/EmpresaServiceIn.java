package com.codigo.msgiraldopalacios.domain.ports.in;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface EmpresaServiceIn {
    ResponseEntity<BaseResponse> crearEmpresaIn(EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> buscarXIdIn(Long id);
    ResponseEntity<BaseResponse> buscarTodosIn();
    ResponseEntity<BaseResponse> actualizarIn(Long id, EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> eliminarIn(Long id);
}
