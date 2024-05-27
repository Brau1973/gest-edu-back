package com.tecnoinf.gestedu.dtos;

import com.tecnoinf.gestedu.models.Docente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  DocenteDTO{

    private Long id;
    private String documento;
    private String nombre;
    private String apellido;

    public DocenteDTO(Docente docente) {
        this.id = docente.getId();
        this.documento = docente.getDocumento();
        this.nombre = docente.getNombre();
        this.apellido = docente.getApellido();
    }
}
