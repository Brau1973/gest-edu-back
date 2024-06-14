package com.tecnoinf.gestedu.services.implementations;

import com.google.firebase.messaging.*;
import com.tecnoinf.gestedu.dtos.NotificacionDTO;
import com.tecnoinf.gestedu.models.Estudiante;
import com.tecnoinf.gestedu.models.Notificacion;
import com.tecnoinf.gestedu.models.Usuario;
import com.tecnoinf.gestedu.repositories.EstudianteRepository;
import com.tecnoinf.gestedu.repositories.NotificacionRepository;
import com.tecnoinf.gestedu.repositories.UsuarioRepository;
import com.tecnoinf.gestedu.services.interfaces.NotificacionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final UsuarioRepository estudianteRepository;
    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(EstudianteRepository estudianteRepository, NotificacionRepository notificacionRepository) {
        this.estudianteRepository = estudianteRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void registrarTokenFirebase(String name, String tokenFirebase){
        Optional<Usuario> usuario = estudianteRepository.findByEmail(name);
        if (usuario.isPresent()) {
            Estudiante estudiante = (Estudiante) usuario.get();
            estudiante.getTokenFirebase().add(tokenFirebase);
            estudianteRepository.save(estudiante);
        }
    }

    @Override
    public void enviarNotificacion(Notificacion notificacion, List<String> tokens) throws FirebaseMessagingException {
        Map<String, String> datos = new HashMap<>();
        datos.put("fecha", notificacion.getFecha().toString());
        datos.put("destinatario", notificacion.getEstudiante().getNombre() + " " + notificacion.getEstudiante().getApellido());

        Notification notification = Notification.builder()
                .setTitle(notificacion.getTitulo())
                .setBody(notificacion.getDescripcion())
                .build();
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(tokens)
                .putAllData(datos)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
    }

    @Override
    public void marcarNotificacionLeida(String name, Long idNotificacion){
        notificacionRepository.findById(idNotificacion).ifPresent(notificacion -> {
            if (notificacion.getEstudiante().getEmail().equals(name)) {
                notificacion.setLeido(true);
                notificacionRepository.save(notificacion);
            }
        });
    }

    @Override
    public List<NotificacionDTO> obtenerNotificaciones(String name){
        ModelMapper modelMapper = new ModelMapper();
        List<Notificacion> notificaciones = notificacionRepository.findByEstudianteEmail(name);
        List<NotificacionDTO> notificacionesDTO = notificaciones.stream()
                .map(notificacion -> modelMapper.map(notificacion, NotificacionDTO.class))
                .collect(Collectors.toList());
        return notificacionesDTO;
    }

    @Override
    public Integer obtenerCantidadNotificacionesNoLeidas(String name){
        List<Notificacion> notificaciones = notificacionRepository.findByEstudianteEmail(name);
        return (int) notificaciones.stream().filter(notificacion -> !notificacion.isLeido()).count();
    }

}
