package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estrutura padrão utilizada para respostas de erro da API")
public class ErroResponseDTO {

    @Schema(
        description = "Código HTTP do erro",
        example = "404"
    )
    private int status;
    
    @Schema(
            description = "Tipo do erro",
            example = "Not Found"
        )
    private String erro;
    
    @Schema(
            description = "Mensagem descritiva do erro",
            example = "Tarefa não encontrada."
        )
    private String mensagem;

    public ErroResponseDTO() {
    }

    public ErroResponseDTO(int status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}