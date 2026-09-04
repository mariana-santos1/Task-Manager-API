package com.mariana.taskmanagerapi.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário " + email + " não encontrado.");
    }

}