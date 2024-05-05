package com.codigo.msgiraldopalacios.application.controller;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.in.EmpresaServiceIn;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ms-giraldo-palacios/v1/empresa")
@AllArgsConstructor
public class EmpresaController {
    private final EmpresaServiceIn empresaServiceIn;

    @PostMapping("/crear")
    public ResponseEntity<BaseResponse> crear(@RequestBody EmpresaRequest empresaRequest){
        return empresaServiceIn.crearEmpresaIn(empresaRequest);
    }

    @GetMapping("/buscarxid/{id}")
    public ResponseEntity<BaseResponse> buscarxid(@PathVariable Long id){
        return empresaServiceIn.buscarXIdIn(id);
    }

    @GetMapping("/todos")
    public ResponseEntity<BaseResponse> todos(){
        return empresaServiceIn.buscarTodosIn();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<BaseResponse> actualizar(@PathVariable Long id, @RequestBody EmpresaRequest empresaRequest){
        return empresaServiceIn.actualizarIn(id, empresaRequest);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return empresaServiceIn.eliminarIn(id);
    }

}
