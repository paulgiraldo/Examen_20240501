package com.codigo.msgiraldopalacios.infraestructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="empresa_info")
@Getter
@Setter
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="razonsocial", nullable = false, length = 255)
    private String razonSocial;

    @Column(name="tipodocumento", nullable = false, length = 5)
    private String tipoDocumento;

    @Column(name="numerodocumento", nullable = false, length = 20, unique = true)
    private String numeroDocumento;

    @Column(name="estado", nullable = false)
    private Integer estado;

    @Column(name="condicion", nullable = false, length = 50)
    private String condicion;

    private String direccion;

    @Column(name="distrito", nullable = true, length = 100)
    private String distrito;

    @Column(name="provincia", nullable = true, length = 100)
    private String provincia;

    @Column(name="departamento", nullable = true, length = 100)
    private String departamento;

    @Column(name="esagenteretencion", nullable = false)
    private boolean EsAgenteRetencion;

    @Column(name = "usuacrea", length = 255)
    private String usuaCrea;

    @Column(name = "datecreate")
    private Timestamp dateCreate;

    @Column(name = "usuamodif", length = 255)
    private String usuaModif;

    @Column(name = "datemodif")
    private Timestamp dateModif;

    @Column(name = "usuadelet", length = 255)
    private String usuaDelet;

    @Column(name = "datedelet")
    private Timestamp dateDelet;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private Set<PersonaEntity> personas;
}
