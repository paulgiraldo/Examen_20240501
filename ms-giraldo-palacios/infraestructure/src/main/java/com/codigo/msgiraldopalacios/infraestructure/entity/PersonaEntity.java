package com.codigo.msgiraldopalacios.infraestructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="persona")
@Getter
@Setter
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name="apellido", nullable = false, length = 255)
    private String apellido;

    @Column(name="tipodocumento", nullable = false, length = 5)
    private String tipoDocumento;

    @Column(name="numerodocumento", nullable = false, length = 20, unique = true)
    private String numeroDocumento;

    @Column(name="email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name="telefono", nullable = true, length = 15)
    private String telefono;

    private String direccion;

    @Column(name="estado", nullable = false)
    private Integer estado;

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

    private Long empresa_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name="empresa_id", nullable = false, insertable=false, updatable=false)
    private EmpresaEntity empresa;

}
