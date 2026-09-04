package com.mariana.taskmanagerapi.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mariana.taskmanagerapi.dto.UsuarioRequestDTO;
import com.mariana.taskmanagerapi.dto.UsuarioResponseDTO;
import com.mariana.taskmanagerapi.entity.Usuario;
import com.mariana.taskmanagerapi.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    private final ModelMapper modelMapper;

    public UsuarioService(
            UsuarioRepository repository,
            ModelMapper modelMapper) {

        this.repository = repository;

        this.modelMapper = modelMapper;
    }

    public UsuarioResponseDTO salvar(
            UsuarioRequestDTO dto) {

        Usuario usuario =
                modelMapper.map(
                        dto,
                        Usuario.class);

        usuario =
                repository.save(usuario);

        return modelMapper.map(
                usuario,
                UsuarioResponseDTO.class);
    }


    public List<UsuarioResponseDTO> listar() {

        return repository.findAll()
                .stream()
                .map(usuario ->
                        modelMapper.map(
                                usuario,
                                UsuarioResponseDTO.class))
                .toList();
    }


    public UsuarioResponseDTO buscarPorId(Long id) {

        Usuario usuario =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuário não encontrado"));

        return modelMapper.map(
                usuario,
                UsuarioResponseDTO.class);
    }


    public UsuarioResponseDTO atualizar(
            Long id,
            UsuarioRequestDTO dto) {

        Usuario usuario =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuário não encontrado"));

        modelMapper.map(dto, usuario);

        usuario =
                repository.save(usuario);

        return modelMapper.map(
                usuario,
                UsuarioResponseDTO.class);
    }


    public void remover(Long id) {

        Usuario usuario =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuário não encontrado"));

        repository.delete(usuario);
    }

}
