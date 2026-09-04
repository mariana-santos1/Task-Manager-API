package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastro de um novo usuário")
public class RegisterRequestDTO {

    @Schema(description = "Nome completo do usuário", example = "Maria Silva", required = true)
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    @Schema(description = "Email do usuário", example = "maria@email.com", required = true)
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;
    
    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public RegisterRequestDTO() {
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}