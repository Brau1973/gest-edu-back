package com.tecnoinf.gestedu.dtos.escolaridad;

import com.tecnoinf.gestedu.dtos.carrera.BasicInfoCarreraDTO;
import com.tecnoinf.gestedu.dtos.usuario.BasicInfoUsuarioDTO;
import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaridadDTO {
    private BasicInfoUsuarioDTO estudiante;
    private BasicInfoCarreraDTO carrera;
    private Integer creditosAprobados;
    private List<EscolaridadSemestreDTO> semestres;
}
