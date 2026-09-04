package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para criar ou atualizar uma tarefa")
public class TarefaRequestDTO {

    @Schema(
        description = "Título da tarefa",
        example = "Estudar Spring Boot",
        required = true
    )
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    private String titulo;

    @Schema(
        description = "Descrição detalhada da tarefa",
        example = "Ler o capítulo sobre Swagger e implementar a documentação",
        required = false
    )
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @Schema(
        description = "Indica se a tarefa já foi concluída",
        example = "false",
        required = true
    )
    private boolean concluida;


    public String getTitulo() {
        return titulo;
    }


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getDescricao() {
        return descricao;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isConcluida() { 
        return concluida; 
    } 
    
    public void setConcluida(boolean concluida) {
        this.concluida = concluida; 
    }

}
