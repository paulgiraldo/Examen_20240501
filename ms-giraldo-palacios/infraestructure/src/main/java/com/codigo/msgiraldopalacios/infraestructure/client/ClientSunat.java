package com.codigo.msgiraldopalacios.infraestructure.client;

import com.codigo.msgiraldopalacios.domain.aggregates.dto.SunatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="client-sunat",url="https://api.apis.net.pe/v2/sunat/")
public interface ClientSunat {
    @GetMapping("/ruc")
    SunatDto getInfoSunat(@RequestParam("numero") String numero,
                          @RequestHeader("Authorization") String authorization);
}
