package com.jorge.projeto.tarefas.tarefas_java_jwt.exception;

public class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message) {
        super(message);
    }
}
