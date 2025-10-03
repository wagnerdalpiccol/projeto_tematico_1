package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.repository.TarefaRepository;
import com.projetotematico1.notesflow.model.entities.Tarefa;

import java.util.List;
import java.util.UUID;

public class TarefaService {

    private TarefaRepository tarefaRepository;

    public TarefaService() {

    }

    public void adicionar(Tarefa tarefa) {
        tarefa.setId(UUID.randomUUID());
        tarefaRepository.adicionar(tarefa);
    }

    public void atualizar(Tarefa tarefa) {
        tarefaRepository.atualizar(tarefa);
    }

    public void deletar(UUID id) {
        tarefaRepository.deletar(id);
    }

    public List<Tarefa> getAll() {
        return tarefaRepository.getAll();
    }
}