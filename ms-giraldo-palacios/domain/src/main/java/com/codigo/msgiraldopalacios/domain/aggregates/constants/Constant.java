package com.codigo.msgiraldopalacios.domain.aggregates.constants;

public class Constant {
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;
    public static final String USU_ADMIN = "PGIRALDO";
    public static final String REDIS_KEY_OBTENERPERSONA="MS:REGISTRO:PERSONA:";
    public static final String REDIS_KEY_OBTENEREMPRESA="MS:REGISTRO:EMPRESA:";

    //Mensajes
    public static final Integer CODE_OK=2001;
    public static final String MSJ_OK="Transacción exitosa";
    public static final Integer CODE_EXIST=1001;
    public static final String MSJ_EXIST_EMPRESA="La empresa ya existe";
    public static final String MSJ_EXIST_PERSONA="La persona ya existe";
    public static final String MSJ_EXIST_EMAIL="El email ya existe";
    public static final Integer CODE_EMPRESA_NO_EXIST=4001;
    public static final String MSJ_EMPRESA_NO_EXIST="No hay datos";

    public static final boolean AGENTE_RETENCION_DEFAULT = false;
    public static final boolean AGENTE_RETENCION_TRUE = true;

    public static final Integer REDIS_EXPIRE_MINUTES=10;
}
