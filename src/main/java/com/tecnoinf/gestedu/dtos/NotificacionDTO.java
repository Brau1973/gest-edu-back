package com.tecnoinf.gestedu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private boolean leido;
}
