package com.tecnoinf.gestedu.dtos.inscripcionExamen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecnoinf.gestedu.dtos.examen.ExamenDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.InscripcionExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionExamenDTO {
    private Long id;
    private UsuarioDTO estudiante;
    private ExamenDTO examen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInscripcion;

    public InscripcionExamenDTO(InscripcionExamen inscripcionExamen) {
        this.id = inscripcionExamen.getId();
        this.estudiante = new UsuarioDTO(inscripcionExamen.getEstudiante());
        this.examen = new ExamenDTO(inscripcionExamen.getExamen());
        this.fechaInscripcion = inscripcionExamen.getFechaInscripcion();
    }
}
