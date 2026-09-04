package com.mariana.taskmanagerapi.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mariana.taskmanagerapi.dto.TarefaRequestDTO;
import com.mariana.taskmanagerapi.dto.TarefaResponseDTO;
import com.mariana.taskmanagerapi.entity.Tarefa;
import com.mariana.taskmanagerapi.entity.Usuario;
import com.mariana.taskmanagerapi.exception.TarefaNaoEncontradaException;
import com.mariana.taskmanagerapi.exception.UsuarioNaoEncontradoException;
import com.mariana.taskmanagerapi.repository.TarefaRepository;
import com.mariana.taskmanagerapi.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;

@Service
public class TarefaService {

    private final TarefaRepository repository;
    
    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper;

    public TarefaService(
        TarefaRepository repository,
        UsuarioRepository usuarioRepository,
        ModelMapper modelMapper) {

        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public boolean isOwner(Authentication authentication, Long id) {

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(email));

        return repository.findByIdAndUsuarioId(
                id,
                usuario.getId()
        ).isPresent();
    }
    public TarefaResponseDTO salvar(
            TarefaRequestDTO dto,
            String email) {

        Usuario usuario =
        usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(email));

        Tarefa tarefa =
                modelMapper.map(
                        dto,
                        Tarefa.class);

        tarefa.setConcluida(false);

        tarefa.setUsuario(usuario);

        tarefa =
                repository.save(tarefa);

        return modelMapper.map(
                tarefa,
                TarefaResponseDTO.class);
    }

    public List<TarefaResponseDTO> listar(){

        return repository.findAll()
                .stream()
                .map(tarefa ->
                        modelMapper.map(
                                tarefa,
                                TarefaResponseDTO.class))
                .toList();

    }
    
    public List<TarefaResponseDTO> listarPorUsuario(String email) {

        Usuario usuario =
        usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(email));

        return repository.findByUsuarioId(usuario.getId())
                .stream()
                .map(tarefa ->
                        modelMapper.map(
                                tarefa,
                                TarefaResponseDTO.class))
                .toList();
    }
    
    public TarefaResponseDTO buscarPorId(Long id, String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        Tarefa tarefa = repository.findByIdAndUsuarioId(
                id,
                usuario.getId()
        ).orElseThrow(() -> new TarefaNaoEncontradaException(id));

        return modelMapper.map(tarefa, TarefaResponseDTO.class);
    }
    
    public TarefaResponseDTO atualizar(
        Long id,
        TarefaRequestDTO dto,
        String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        Tarefa tarefa = repository.findByIdAndUsuarioId(
                id,
                usuario.getId()
        ).orElseThrow(() -> new TarefaNaoEncontradaException(id));

        modelMapper.map(dto, tarefa);

        tarefa = repository.save(tarefa);

        return modelMapper.map(tarefa, TarefaResponseDTO.class);
    }
    
    public void remover(Long id, String email) {

    Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

    Tarefa tarefa = repository.findByIdAndUsuarioId(
            id,
            usuario.getId()
    ).orElseThrow(() -> new TarefaNaoEncontradaException(id));

    repository.delete(tarefa);
}

}