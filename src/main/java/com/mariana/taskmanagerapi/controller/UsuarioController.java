package com.mariana.taskmanagerapi.controller;

import com.mariana.taskmanagerapi.dto.UsuarioRequestDTO;
import com.mariana.taskmanagerapi.dto.UsuarioResponseDTO;
import com.mariana.taskmanagerapi.service.UsuarioService;
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

@Tag(
    name = "Usuários",
    description = "Operações relacionadas ao gerenciamento de usuários"
)
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(
        summary = "Cadastrar usuário",
        description = "Cria um novo usuário no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "E-mail já cadastrado"
        )
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        UsuarioResponseDTO resposta = service.salvar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @Operation(
        summary = "Listar usuários",
        description = "Retorna a lista de usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso"
        )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {

        return ResponseEntity.ok(service.listar());
    }

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os dados de um usuário específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(
            @Parameter(
                description = "ID do usuário",
                example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "E-mail já cadastrado"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @Parameter(
                description = "ID do usuário",
                example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(
        summary = "Deletar usuário",
        description = "Remove um usuário existente do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuário removido com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @Parameter(
                description = "ID do usuário",
                example = "1"
            )
            @PathVariable Long id) {

        service.remover(id);

        return ResponseEntity.noContent().build();
    }
}