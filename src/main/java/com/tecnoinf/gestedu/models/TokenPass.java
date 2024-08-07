package com.tecnoinf.gestedu.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TokenPass {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime vencimiento;
    private Boolean activo;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;
}
