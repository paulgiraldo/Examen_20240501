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

    @Column(name="tipoDocumento", nullable = false, length = 5)
    private String tipoDocumento;

    @Column(name="numeroDocumento", nullable = false, length = 20, unique = true)
    private String numeroDocumento;

    @Column(name="email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name="telefono", nullable = true, length = 15)
    private String telefono;

    private String direccion;

    @Column(name="estado", nullable = false)
    private Integer estado;

    @Column(name = "usuaCrea", length = 255)
    private String usuaCrea;

    @Column(name = "dateCreate")
    private Timestamp dateCreate;

    @Column(name = "usuaModif", length = 255)
    private String usuaModif;

    @Column(name = "dateModif")
    private Timestamp dateModif;

    @Column(name = "usuaDelet", length = 255)
    private String usuaDelet;

    @Column(name = "dateDelet")
    private Timestamp dateDelet;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="empresa_id")
    private EmpresaEntity empresa;

}
