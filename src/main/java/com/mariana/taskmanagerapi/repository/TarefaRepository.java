package com.mariana.taskmanagerapi.repository;

import com.mariana.taskmanagerapi.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository
        extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByConcluida(boolean concluida);

    List<Tarefa> findByUsuarioId(Long id);

    List<Tarefa> findByUsuarioIdAndConcluida(
            Long id,
            boolean concluida);

    Optional<Tarefa> findByIdAndUsuarioId(
            Long id,
            Long usuarioId);

    @Query("""
            SELECT t
            FROM Tarefa t
            WHERE t.usuario.id = :usuarioId
            ORDER BY t.titulo
            """)
    List<Tarefa> listarOrdenadas(Long usuarioId);
}