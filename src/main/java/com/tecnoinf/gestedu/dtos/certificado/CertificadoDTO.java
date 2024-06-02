package com.tecnoinf.gestedu.dtos.certificado;

import com.tecnoinf.gestedu.dtos.usuario.UsuarioDTO;
import com.tecnoinf.gestedu.models.Certificado;
import com.tecnoinf.gestedu.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificadoDTO {
    private Long id;
    private UsuarioDTO estudiante;
    private String codigoValidacion;
    private String carrera;
    private String fecha;

    public CertificadoDTO(Certificado certificado) {
        this.id = certificado.getId();
        this.estudiante = new UsuarioDTO(certificado.getEstudiante());
        this.codigoValidacion = certificado.getCodigoValidacion();
        this.carrera = certificado.getCarrera();
        this.fecha = certificado.getFecha().toString();
    }
}
