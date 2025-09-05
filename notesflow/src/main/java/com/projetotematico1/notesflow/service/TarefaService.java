package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.dao.TarefaDAO;
import com.projetotematico1.notesflow.model.Tarefa;

import java.util.UUID;

public class TarefaService {

    private final TarefaDAO tarefaDAO;

    public TarefaService(TarefaDAO tarefaDAO) {
        this.tarefaDAO = tarefaDAO;
    }

    public void adicionar(Tarefa tarefa) {
        tarefa.setId(UUID.randomUUID());
        tarefaDAO.adicionar(tarefa);
    }

    public void atualizar(Tarefa tarefa) {
        tarefaDAO.atualizar(tarefa);
    }

    public void deletar(UUID id) {
        tarefaDAO.deletar(id);
    }
}
