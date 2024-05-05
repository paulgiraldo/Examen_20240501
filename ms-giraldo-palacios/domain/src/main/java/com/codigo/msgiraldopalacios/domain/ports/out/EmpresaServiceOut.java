package com.codigo.msgiraldopalacios.domain.ports.out;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface EmpresaServiceOut {
    ResponseEntity<BaseResponse> crearEmpresaOut(EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> buscarXIdOut(Long id);
    ResponseEntity<BaseResponse> buscarTodosOut();
    ResponseEntity<BaseResponse> actualizarOut(Long id, EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> eliminarOut(Long id);
}
