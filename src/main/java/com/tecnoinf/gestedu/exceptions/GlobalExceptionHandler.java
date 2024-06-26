package com.tecnoinf.gestedu.exceptions;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            TramiteNotFoundException.class,
            ResourceNotFoundException.class,
            TokenInactivoException.class,
            TokenVencidoException.class,
            TokenInvalidoException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            UniqueFieldException.class,
            PlanEstudioNoExisteException.class,
            SemestreException.class,
            AsignaturaPreviaExistenteException.class,
            CicloEnAsignaturasException.class,
            FechaException.class,
            TramitePendienteExistenteException.class,
            TramiteNotPendienteException.class,
            MessagingException.class,
            PeriodoInscripcionExeption.class,
            InscripcionExamenException.class,
            CalificacionExamenExeption.class,
            BajaExamenException.class,
            BajaDocenteException.class,
            })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("trace", "");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}