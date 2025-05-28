package com.jorge.projeto.tarefas.tarefas_java_jwt.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jorge.projeto.tarefas.tarefas_java_jwt.exception.TokenCreationException;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    //.withClaim("role", user.getRole().name())
                    //.withClaim("role", "ROLE_" + user.getRole().name())
                    //.withClaim("roles", List.of("ROLE_" + user.getRole().name()))
                    .withClaim("role", user.getRole().name())
                    .withClaim("roles", List.of(user.getRole().name()))
                    .withClaim("id", user.getId())
                    .withExpiresAt(this.generationExpirationDate())
                    .sign(algorithm);
            return  token;
        } catch (JWTCreationException e) {
            throw new TokenCreationException("Erro ao gerar token");
        }
    }
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
    private Instant generationExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}