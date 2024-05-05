package com.codigo.msgiraldopalacios.domain.impl;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.in.EmpresaServiceIn;
import com.codigo.msgiraldopalacios.domain.ports.out.EmpresaServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmpresaServiceImpl implements EmpresaServiceIn {

    private final EmpresaServiceOut empresaServiceOut;
    @Override
    public ResponseEntity<BaseResponse> crearEmpresaIn(EmpresaRequest empresaRequest) {
        return empresaServiceOut.crearEmpresaOut(empresaRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> buscarXIdIn(Long id) {
        return empresaServiceOut.buscarXIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> buscarTodosIn() {
        return empresaServiceOut.buscarTodosOut();
    }

    @Override
    public ResponseEntity<BaseResponse> actualizarIn(Long id, EmpresaRequest empresaRequest) {
        return empresaServiceOut.actualizarOut(id, empresaRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> eliminarIn(Long id) {
        return empresaServiceOut.eliminarOut(id);
    }
}
