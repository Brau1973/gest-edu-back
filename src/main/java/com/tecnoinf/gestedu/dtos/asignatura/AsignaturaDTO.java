package com.tecnoinf.gestedu.dtos.asignatura;

import com.tecnoinf.gestedu.models.Asignatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private Integer semestrePlanEstudio;
    private Long carreraId;

    public AsignaturaDTO(Asignatura asignatura) {
        this.id = asignatura.getId();
        this.nombre = asignatura.getNombre();
        this.descripcion = asignatura.getDescripcion();
        this.creditos = asignatura.getCreditos();
        this.semestrePlanEstudio = asignatura.getSemestrePlanEstudio();
        this.carreraId = asignatura.getCarrera().getId();
    }
}
