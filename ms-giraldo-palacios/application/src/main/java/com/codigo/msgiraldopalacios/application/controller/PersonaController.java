package com.codigo.msgiraldopalacios.application.controller;

import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.request.PersonaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.in.EmpresaServiceIn;
import com.codigo.msgiraldopalacios.domain.ports.in.PersonaServiceIn;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ms-giraldo-palacios/v1/persona")
@AllArgsConstructor
public class PersonaController {
    private final PersonaServiceIn personaServiceIn;

    @PostMapping("/crearPersona")
    public ResponseEntity<BaseResponse> crear(@RequestBody PersonaRequest personaRequest){
        return personaServiceIn.crearPersonaIn(personaRequest);
    }

    @GetMapping("/buscarxId/{id}")
    public ResponseEntity<BaseResponse> buscarxid(@PathVariable Long id){
        return personaServiceIn.buscarXIdIn(id);
    }

    @GetMapping("/buscartodos")
    public ResponseEntity<BaseResponse> todos(){
        return personaServiceIn.buscarTodosIn();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<BaseResponse> actualizar(@PathVariable Long id, @RequestBody PersonaRequest personaRequest){
        return personaServiceIn.actualizarIn(id, personaRequest);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<BaseResponse> eliminar(@PathVariable Long id){
        return personaServiceIn.eliminarIn(id);
    }

}
