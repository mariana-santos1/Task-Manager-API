package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public class LoginRequestDTO {

    @Schema(
        description = "Email do usuário",
        example = "usuario@email.com",
        required = true
    )
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(
        description = "Senha do usuário",
        example = "123456",
        required = true
    )
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public LoginRequestDTO() {
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