package com.codigo.msgiraldopalacios.infraestructure.adapters;

import com.codigo.msgiraldopalacios.domain.aggregates.constants.Constant;
import com.codigo.msgiraldopalacios.domain.aggregates.dto.ReniecDto;
import com.codigo.msgiraldopalacios.domain.aggregates.dto.SunatDto;
import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.request.PersonaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.out.PersonaServiceOut;
import com.codigo.msgiraldopalacios.infraestructure.client.ClientReniec;
import com.codigo.msgiraldopalacios.infraestructure.client.ClientSunat;
import com.codigo.msgiraldopalacios.infraestructure.dao.EmpresaRepository;
import com.codigo.msgiraldopalacios.infraestructure.dao.PersonaRepository;
import com.codigo.msgiraldopalacios.infraestructure.entity.EmpresaEntity;
import com.codigo.msgiraldopalacios.infraestructure.entity.PersonaEntity;
import com.codigo.msgiraldopalacios.infraestructure.mapper.EmpresaMapper;
import com.codigo.msgiraldopalacios.infraestructure.mapper.PersonaMapper;
import com.codigo.msgiraldopalacios.infraestructure.redis.RedisService;
import com.codigo.msgiraldopalacios.infraestructure.util.Util;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaAdapter implements PersonaServiceOut {
    private final PersonaRepository personaRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientReniec clientReniec;
    private final RedisService redisService;

    @Value("${token.reniec}")
    private String tokenReniec;

    @Override
    public ResponseEntity<BaseResponse> crearPersonaOut(PersonaRequest personaRequest) {
        // Validando que se ingrese un valor de RUC en el request
        if (ObjectUtils.isEmpty(personaRequest) || personaRequest.getNumDoc().isEmpty() || personaRequest.getEmail().isEmpty() || personaRequest.getEmpresa_id()==0 ){
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_PERSONA_VALIDA01, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
        if (personaRepository.existsByNumeroDocumento(personaRequest.getNumDoc())) {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_PERSONA, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
        if (personaRepository.existsByEmail(personaRequest.getEmail())) {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_EMAIL, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
        if (!empresaRepository.existsById(  Long.valueOf(personaRequest.getEmpresa_id() ) )) {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_NOEXIST_EMPRESA, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }


        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity = getEntity(personaRequest, personaEntity) ;
        // Validando que la RENIEC localice el DNI enviado
        if (ObjectUtils.isEmpty(personaEntity.getNombre())) {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        } else {
            personaEntity.setEstado(Constant.STATUS_ACTIVE);
            personaEntity.setUsuaCrea(Constant.USU_ADMIN);
            personaEntity.setDateCreate(getTimestamp());

            BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(PersonaMapper.fromEntity(personaRepository.save(personaEntity))));
            return ResponseEntity.ok(baseResponse);
        }
    }

    private PersonaEntity getEntity(PersonaRequest personaRequest, PersonaEntity entity){
        //Exec servicio
        try{
            ReniecDto reniecDto = getExecReniec(personaRequest.getNumDoc());
            entity.setNombre(reniecDto.getNombres());
            entity.setApellido(reniecDto.getApellidoPaterno()+' '+reniecDto.getApellidoMaterno());
            entity.setTipoDocumento(personaRequest.getTipoDoc());
            entity.setNumeroDocumento(reniecDto.getNumeroDocumento());
            entity.setEmail(personaRequest.getEmail());
            entity.setTelefono(personaRequest.getTelefono());
            entity.setDireccion(personaRequest.getDireccion());
            entity.setEmpresa_id(personaRequest.getEmpresa_id());
            return entity;
        } catch (FeignException e){
            return new PersonaEntity();
        }
    }

    private ReniecDto getExecReniec(String numDoc){
        String authorization = "Bearer "+tokenReniec;
        return clientReniec.getInfoReniec(numDoc,authorization);
    }

    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }


    @Override
    public ResponseEntity<BaseResponse> buscarXIdOut(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERPERSONA+id);
        if (redisInfo!=null){
            baseResponse.setCode(Constant.CODE_OK);
            baseResponse.setMessage(Constant.MSJ_OK);
            baseResponse.setDto(Optional.of(PersonaMapper.fromEntity(Util.convertirDesdeString(redisInfo,PersonaEntity.class))));
        } else {
            Optional<PersonaEntity> personaBuscar = personaRepository.findById(id);
            if(personaBuscar.isPresent()){
                baseResponse.setCode(Constant.CODE_OK);
                baseResponse.setMessage(Constant.MSJ_OK);
                baseResponse.setDto(Optional.of(PersonaMapper.fromEntity(personaBuscar.get())));

                String dataForRedis = Util.convertirAString(personaBuscar.get());
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENERPERSONA+id,dataForRedis,Constant.REDIS_EXPIRE_MINUTES);
            }else{
                baseResponse.setCode(Constant.CODE_EMPRESA_NO_EXIST);
                baseResponse.setMessage(Constant.MSJ_EMPRESA_NO_EXIST);
                baseResponse.setDto(Optional.empty());
            }

        }
        return ResponseEntity.ok(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> buscarTodosOut() {
        BaseResponse baseResponse = new BaseResponse();
        List<PersonaEntity> listPersona = personaRepository.findAll();

        if(!listPersona.isEmpty()){
            baseResponse.setCode(Constant.CODE_OK);
            baseResponse.setMessage(Constant.MSJ_OK);
            baseResponse.setDto(Optional.of(listPersona));
        }else {
            baseResponse.setCode(Constant.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constant.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setDto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);

    }

    @Override
    public ResponseEntity<BaseResponse> actualizarOut(Long id, PersonaRequest personaRequest) {
        // Validando que se ingrese un valor de RUC en el request
        if (ObjectUtils.isEmpty(personaRequest) || personaRequest.getNumDoc().isEmpty() || personaRequest.getEmail().isEmpty() || personaRequest.getEmpresa_id()==0 ){
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_PERSONA_VALIDA01, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
        Optional<PersonaEntity> datoExtraido = personaRepository.findById(id);
        if (datoExtraido.isPresent()) {
            // si se ha ingresado un nuevo DNI, validar que no exista en la BD
            if (!datoExtraido.get().getNumeroDocumento().equals(personaRequest.getNumDoc())){
                if (personaRepository.existsByNumeroDocumento(personaRequest.getNumDoc())){
                    BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_PERSONA, Optional.empty());
                    return ResponseEntity.ok(baseResponse);
                }
            }
            // si se ha ingresado un nuevo EMAIL, validar que no exista en la BD
            if (!datoExtraido.get().getEmail().equals(personaRequest.getEmail())){
                if (personaRepository.existsByEmail(personaRequest.getEmail())){
                    BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_EMAIL, Optional.empty());
                    return ResponseEntity.ok(baseResponse);
                }
            }
            // si se ha ingresado un nuevo EMPRESA_ID, validar que no exista en la BD
            if (!datoExtraido.get().getEmpresa_id().equals(personaRequest.getEmpresa_id())){
                if (!empresaRepository.existsById(personaRequest.getEmpresa_id())){
                    BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_NOEXIST_EMPRESA, Optional.empty());
                    return ResponseEntity.ok(baseResponse);
                }
            }

            PersonaEntity personaEntity = datoExtraido.get();
            personaEntity = getEntity(personaRequest, personaEntity) ;
            // Validando que la RENIEC localice el DNI enviado
            if (ObjectUtils.isEmpty(personaEntity.getNombre())) {
                BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
                return ResponseEntity.ok(baseResponse);
            } else {
                personaEntity.setUsuaModif(Constant.USU_ADMIN);
                personaEntity.setDateModif(getTimestamp());

                BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(PersonaMapper.fromEntity(personaRepository.save(personaEntity))));

                // Eliminar el dato en redis, por la modificacion
                redisService.deleteKey(Constant.REDIS_KEY_OBTENERPERSONA+id);
                return ResponseEntity.ok(baseResponse);
            }

        } else {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }

    }

    @Override
    public ResponseEntity<BaseResponse> eliminarOut(Long id) {
        Optional<PersonaEntity> datoExtraido = personaRepository.findById(id);
        if(datoExtraido.isPresent()){
            datoExtraido.get().setEstado(Constant.STATUS_INACTIVE);
            datoExtraido.get().setUsuaDelet(Constant.USU_ADMIN);
            datoExtraido.get().setDateDelet(getTimestamp());
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(PersonaMapper.fromEntity(personaRepository.save(datoExtraido.get()))));

            // Eliminar el dato en redis, por la modificacion
            redisService.deleteKey(Constant.REDIS_KEY_OBTENERPERSONA+id);

            return ResponseEntity.ok(baseResponse);
        }else {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }
}
