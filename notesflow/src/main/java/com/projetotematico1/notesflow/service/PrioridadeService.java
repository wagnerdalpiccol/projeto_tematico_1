package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.model.entities.Prioridade;
import com.projetotematico1.notesflow.model.repository.PrioridadeRepository;

import java.util.List;
import java.util.UUID;

public class PrioridadeService {

    private final PrioridadeRepository repository = new PrioridadeRepository();

    // busca todas as prioridades
    public List<Prioridade> findAll() {
        return repository.findAll();
    }

    // salvar ou atualizar
    public Prioridade save(Prioridade prioridade) {
        if (prioridade.getDescricao() == null || prioridade.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da prioridade é obrigatória.");
        }

        // Se for um novo registro, gera um novo UUID
        if (prioridade.getId() == null) {
            prioridade.setId(UUID.randomUUID());
        }

        return repository.save(prioridade);
    }

    public Prioridade update(Prioridade prioridade) {
        return save(prioridade);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}