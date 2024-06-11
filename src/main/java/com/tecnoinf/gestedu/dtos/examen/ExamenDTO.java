package com.tecnoinf.gestedu.dtos.examen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecnoinf.gestedu.dtos.DocenteDTO;
import com.tecnoinf.gestedu.dtos.asignatura.AsignaturaDTO;
import com.tecnoinf.gestedu.models.Asignatura;
import com.tecnoinf.gestedu.models.Docente;
import com.tecnoinf.gestedu.models.Examen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fecha;
    private Integer diasPrevInsc;
    private AsignaturaDTO asignatura;
    private List<DocenteDTO> docentes;

    public ExamenDTO(Examen examen) {
        this.id = examen.getId();
        this.fecha = examen.getFecha();
        this.diasPrevInsc = examen.getDiasPrevInsc();
        this.asignatura = new AsignaturaDTO(examen.getAsignatura());
        this.docentes = new ArrayList<>();
        if (examen.getDocentes() != null) {
            for(Docente docente : examen.getDocentes()) {
                this.docentes.add(new DocenteDTO(docente));
            }
        }
    }
}
