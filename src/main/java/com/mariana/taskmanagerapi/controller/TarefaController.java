package com.mariana.taskmanagerapi.controller;

import com.mariana.taskmanagerapi.dto.TarefaRequestDTO;
import com.mariana.taskmanagerapi.dto.TarefaResponseDTO;
import com.mariana.taskmanagerapi.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Tag(name = "Tarefas", description = "Operações relacionadas ao gerenciamento de tarefas")
@RestController
@RequestMapping("/api/v1/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @Operation(
        summary = "Cadastrar tarefa",
        description = "Cria uma nova tarefa para o usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TarefaResponseDTO> cadastrar(
        @Parameter(description = "Dados da tarefa a ser criada")
        @Valid @RequestBody TarefaRequestDTO dto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        TarefaResponseDTO resposta =
                service.salvar(dto, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }

    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas do usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TarefaResponseDTO>> listar() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        List<TarefaResponseDTO> tarefas =
                service.listarPorUsuario(email);

        return ResponseEntity.ok(tarefas);
    }
    

    @Operation(
        summary = "Buscar tarefa por ID",
        description = "Retorna uma tarefa específica do usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize(
        "hasRole('USER') and @tarefaService.isOwner(authentication, #id)"
    )
    public ResponseEntity<TarefaResponseDTO> buscar(
            @Parameter(description = "ID da tarefa")
            @PathVariable Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.buscarPorId(id, email)
        );
    }

    @Operation(
        summary = "Atualizar tarefa",
        description = "Atualiza uma tarefa existente do usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário não autorizado a modificar esta tarefa"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize(
        "hasRole('USER') and @tarefaService.isOwner(authentication, #id)"
    )
    public ResponseEntity<TarefaResponseDTO> atualizar(
            @Parameter(description = "ID da tarefa")
            @PathVariable Long id,

            @Parameter(description = "Novos dados da tarefa")
            @Valid @RequestBody TarefaRequestDTO dto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.atualizar(id, dto, email)
        );
    }

    @Operation(
        summary = "Deletar tarefa",
        description = "Remove uma tarefa existente do usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa removida com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário não autorizado a remover esta tarefa"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize(
        "hasRole('USER') and @tarefaService.isOwner(authentication, #id)"
    )
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID da tarefa")
            @PathVariable Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        service.remover(id, email);

        return ResponseEntity.noContent().build();
    }
}