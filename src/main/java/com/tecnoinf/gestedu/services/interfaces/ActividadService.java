package com.tecnoinf.gestedu.services.interfaces;

import com.tecnoinf.gestedu.dtos.ActividadDTO;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.TipoActividad;

import java.util.List;

public interface ActividadService {
    void registrarActividad(TipoActividad tipoActividad, String descripcion);
    List<ActividadDTO> getActividadByUsuarioId(Long id);
}
