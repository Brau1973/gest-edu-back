package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String ci;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Column(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String telefono;
    private String domicilio;
    private Date fechaNac;
    private String imagen;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TokenPass> tokenPass = new HashSet<>();

    @Column(name= "is_enable")
    private Boolean isEnable;

    @Column (name= "account_non_expired")
    private Boolean accountNonExpired;

    @Column (name= "account_non_locked")
    private Boolean accountNonLocked;

    @Column (name= "credentials_non_expired")
    private Boolean credentialsNonExpired;

}
