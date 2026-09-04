package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário retornados pela API")
public class UsuarioResponseDTO {

    @Schema(
        description = "Identificador único do usuário",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Nome do usuário",
        example = "Maria"
    )
    private String nome;

    @Schema(
        description = "E-mail do usuário",
        example = "maria@email.com"
    )
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}