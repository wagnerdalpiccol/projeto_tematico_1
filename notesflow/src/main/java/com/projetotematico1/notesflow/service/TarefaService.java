package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.repository.TarefaRepository;
import com.projetotematico1.notesflow.model.entities.Tarefa;

import java.util.List;
import java.util.UUID;

public class TarefaService {

    private TarefaRepository tarefaRepository;

    public TarefaService() {
        this.tarefaRepository = new TarefaRepository();
    }

    public void save(Tarefa tarefa) {
        tarefa.setId(UUID.randomUUID());
        tarefaRepository.save(tarefa);
    }

    public void update(Tarefa tarefa) {
        tarefaRepository.update(tarefa);
    }

    public void delete(UUID id) {
        tarefaRepository.delete(id);
    }

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }
}