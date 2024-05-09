package com.tecnoinf.gestedu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tecnoinf.gestedu.models.ListaNegraToken;
import com.tecnoinf.gestedu.repositories.ListaNegraTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    @Autowired
    private ListaNegraTokenRepository listaNegraTokenRepository;

    public String crearToken(Authentication authentication) {
        Algorithm algoritmo = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) //vence en 30 minutos
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algoritmo);
        return jwtToken;
    }

    public DecodedJWT validarToken(String token) {
        try{
            ListaNegraToken listaNegraToken = listaNegraTokenRepository.findByToken(token);
            if (listaNegraToken != null) {
                throw new JWTVerificationException("Token en lista negra");
            }

            Algorithm algoritmo = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algoritmo)
                    .withIssuer(this.userGenerator)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token no válido");
        }
    }

    public String obtenerUsuarioToken(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claim) {
        return decodedJWT.getClaim(claim);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    public void listaNegraToken(String token) {
        DecodedJWT decodedJWT = validarToken(token);
        ListaNegraToken blacklistedToken = new ListaNegraToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setFechaExpiracion(decodedJWT.getExpiresAt().toInstant());
        listaNegraTokenRepository.save(blacklistedToken);
    }
}
