package com.codigo.msgiraldopalacios.infraestructure.adapters;

import com.codigo.msgiraldopalacios.domain.aggregates.constants.Constant;
import com.codigo.msgiraldopalacios.domain.aggregates.dto.SunatDto;
import com.codigo.msgiraldopalacios.domain.aggregates.request.EmpresaRequest;
import com.codigo.msgiraldopalacios.domain.aggregates.response.BaseResponse;
import com.codigo.msgiraldopalacios.domain.ports.out.EmpresaServiceOut;
import com.codigo.msgiraldopalacios.infraestructure.client.ClientSunat;
import com.codigo.msgiraldopalacios.infraestructure.dao.EmpresaRepository;
import com.codigo.msgiraldopalacios.infraestructure.entity.EmpresaEntity;
import com.codigo.msgiraldopalacios.infraestructure.mapper.EmpresaMapper;
import com.codigo.msgiraldopalacios.infraestructure.redis.RedisService;
import com.codigo.msgiraldopalacios.infraestructure.util.Util;
import feign.FeignException;
import io.netty.util.internal.ObjectUtil;
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
public class EmpresaAdapter implements EmpresaServiceOut {

    private final EmpresaRepository empresaRepository;
    private final ClientSunat clientSunat;
    private final RedisService redisService;

    @Value("${token.reniec}")
    private String tokenReniec;

    @Override
    public ResponseEntity<BaseResponse> crearEmpresaOut(EmpresaRequest empresaRequest) {
        // Validando que se ingrese un valor de RUC en el request
        if (ObjectUtils.isEmpty(empresaRequest) || empresaRequest.getNumDoc().isEmpty()){
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }

        boolean exist = empresaRepository.existsByNumeroDocumento(empresaRequest.getNumDoc());
        if (exist) {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_EMPRESA, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        } else {
            EmpresaEntity empresaEntity = new EmpresaEntity();
            empresaEntity = getEntity(empresaRequest, empresaEntity) ;
            // Validando que la SUNAT localice el RUC enviado
            if (ObjectUtils.isEmpty(empresaEntity.getRazonSocial())) {
                BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
                return ResponseEntity.ok(baseResponse);
            } else {
            empresaEntity.setEstado(Constant.STATUS_ACTIVE);
            empresaEntity.setUsuaCrea(Constant.USU_ADMIN);
            empresaEntity.setDateCreate(getTimestamp());

            BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(EmpresaMapper.fromEntity(empresaRepository.save(empresaEntity))));
            return ResponseEntity.ok(baseResponse);
            }
        }
    }

    private EmpresaEntity getEntity(EmpresaRequest empresaRequest, EmpresaEntity entity){
        //Exec servicio
        try{
            SunatDto sunatDto = getExecSunat(empresaRequest.getNumDoc());
            entity.setRazonSocial(sunatDto.getRazonSocial());
            entity.setTipoDocumento(empresaRequest.getTipoDoc());
            entity.setNumeroDocumento(sunatDto.getNumeroDocumento());
            entity.setCondicion(sunatDto.getCondicion());
            entity.setDireccion(sunatDto.getDireccion());
            entity.setDistrito(sunatDto.getDistrito());
            entity.setProvincia(sunatDto.getProvincia());
            entity.setDepartamento(sunatDto.getDepartamento());
            entity.setEsAgenteRetencion(sunatDto.isEsAgenteRetencion());
            return entity;
        } catch (FeignException e){
            return new EmpresaEntity();
        }
    }

    private SunatDto getExecSunat(String numDoc){
        String authorization = "Bearer "+tokenReniec;
        return clientSunat.getInfoSunat(numDoc,authorization);
    }

    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }


    @Override
    public ResponseEntity<BaseResponse> buscarXIdOut(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id);
        if (redisInfo!=null){
            baseResponse.setCode(Constant.CODE_OK);
            baseResponse.setMessage(Constant.MSJ_OK);
            baseResponse.setDto(Optional.of(EmpresaMapper.fromEntity(Util.convertirDesdeString(redisInfo,EmpresaEntity.class))));
        } else {
            Optional<EmpresaEntity> empresaBuscar = empresaRepository.findById(id);
            if(empresaBuscar.isPresent()){
                baseResponse.setCode(Constant.CODE_OK);
                baseResponse.setMessage(Constant.MSJ_OK);
                baseResponse.setDto(Optional.of(EmpresaMapper.fromEntity(empresaBuscar.get())));

                String dataForRedis = Util.convertirAString(empresaBuscar.get());
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id,dataForRedis,Constant.REDIS_EXPIRE_MINUTES);
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
        List<EmpresaEntity> listEmpresa = empresaRepository.findAll();

        if(!listEmpresa.isEmpty()){
            baseResponse.setCode(Constant.CODE_OK);
            baseResponse.setMessage(Constant.MSJ_OK);
            baseResponse.setDto(Optional.of(listEmpresa));
        }else {
            baseResponse.setCode(Constant.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constant.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setDto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);

    }

    @Override
    public ResponseEntity<BaseResponse> actualizarOut(Long id, EmpresaRequest empresaRequest) {
        // Validando que se ingrese un valor de RUC en el request
        if (ObjectUtils.isEmpty(empresaRequest) || empresaRequest.getNumDoc().isEmpty()){
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }

        Optional<EmpresaEntity> datoExtraido = empresaRepository.findById(id);
        if (datoExtraido.isPresent()) {
            // si se ha ingresado un nuevo ruc, validar que no exista en la BD
            if (!datoExtraido.get().getNumeroDocumento().equals(empresaRequest.getNumDoc())){
                if (empresaRepository.existsByNumeroDocumento(empresaRequest.getNumDoc())){
                    BaseResponse baseResponse = new BaseResponse(Constant.CODE_EXIST, Constant.MSJ_EXIST_EMPRESA, Optional.empty());
                    return ResponseEntity.ok(baseResponse);
                }
            }

            EmpresaEntity empresaEntity = datoExtraido.get();
            empresaEntity = getEntity(empresaRequest, empresaEntity) ;
            // Validando que la SUNAT localice el RUC enviado
            if (ObjectUtils.isEmpty(empresaEntity.getRazonSocial())) {
                BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
                return ResponseEntity.ok(baseResponse);
            } else {
                empresaEntity.setUsuaModif(Constant.USU_ADMIN);
                empresaEntity.setDateModif(getTimestamp());

                BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(EmpresaMapper.fromEntity(empresaRepository.save(empresaEntity))));

                // Eliminar el dato en redis, por la modificacion
                redisService.deleteKey(Constant.REDIS_KEY_OBTENEREMPRESA+id);
                return ResponseEntity.ok(baseResponse);
            }

        } else {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> eliminarOut(Long id) {
        Optional<EmpresaEntity> datoExtraido = empresaRepository.findById(id);
        if(datoExtraido.isPresent()){
            datoExtraido.get().setEstado(Constant.STATUS_INACTIVE);
            datoExtraido.get().setUsuaDelet(Constant.USU_ADMIN);
            datoExtraido.get().setDateDelet(getTimestamp());
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_OK, Constant.MSJ_OK, Optional.of(EmpresaMapper.fromEntity(empresaRepository.save(datoExtraido.get()))));

            // Eliminar el dato en redis, por la modificacion
            redisService.deleteKey(Constant.REDIS_KEY_OBTENEREMPRESA+id);

            return ResponseEntity.ok(baseResponse);
        }else {
            BaseResponse baseResponse = new BaseResponse(Constant.CODE_EMPRESA_NO_EXIST, Constant.MSJ_EMPRESA_NO_EXIST, Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }


    }
}
