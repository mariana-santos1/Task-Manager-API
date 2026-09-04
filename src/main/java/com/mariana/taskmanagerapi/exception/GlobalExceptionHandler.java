package com.mariana.taskmanagerapi.exception;

import com.mariana.taskmanagerapi.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> tratarTarefaNaoEncontrada(
            TarefaNaoEncontradaException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                404,
                "Not Found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                404,
                "Not Found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarEmailJaCadastrado(
            EmailJaCadastradoException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                409,
                "Conflict",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(
            MethodArgumentNotValidException exception) {

        String mensagem = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        ErroResponseDTO erro = new ErroResponseDTO(
                400,
                "Bad Request",
                mensagem
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponseDTO> tratarCredenciaisInvalidas(
            BadCredentialsException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                401,
                "Unauthorized",
                "E-mail ou senha inválidos."
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(
            Exception exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                500,
                "Internal Server Error",
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }
    
}