package com.tecnoinf.gestedu.repositories;

import com.tecnoinf.gestedu.dtos.Tramite.TramiteDTO;
import com.tecnoinf.gestedu.models.Carrera;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Tramite;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.models.enums.EstadoTramite;
import com.tecnoinf.gestedu.models.enums.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TramiteRepository extends JpaRepository<Tramite, Long>, JpaSpecificationExecutor<Carrera> {
    boolean existsByUsuarioSolicitanteAndCarreraAndTipoAndEstado(Usuario usuarioSolicitante, Carrera carrera, TipoTramite tipo, EstadoTramite estado);

    List<Tramite> findAllByTipoAndEstado(TipoTramite tipoTramite, EstadoTramite estadoTramite);

    List<Tramite> findAllByUsuarioSolicitante(Estudiante estudiante);
}

