package com.mariana.taskmanagerapi.exception;

public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }

}