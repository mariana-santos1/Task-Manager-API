package com.mariana.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de uma tarefa")
public class TarefaResponseDTO {

    @Schema(
        description = "Identificador único da tarefa", 
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Título da tarefa", 
        example = "Estudar Spring Boot"
    )
    private String titulo;

    @Schema(
        description = "Descrição detalhada da tarefa", 
        example = "Ler o capítulo sobre Swagger"
    )
    private String descricao;

    @Schema(
        description = "Indica se a tarefa foi concluída", 
        example = "false"
    )
    private boolean concluida;

    public TarefaResponseDTO() {
    }

    public TarefaResponseDTO(
            Long id,
            String titulo,
            String descricao,
            boolean concluida) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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