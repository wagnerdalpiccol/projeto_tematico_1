package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.model.entities.Lista;
import com.projetotematico1.notesflow.model.repository.ListaRepository;

import java.util.List;
import java.util.UUID;

public class ListaService {

    private final ListaRepository repository = new ListaRepository();

    public List<Lista> findAll() {
        return repository.findAll();
    }

    public Lista save(Lista lista) {
        if (lista.getNomelista() == null || lista.getNomelista().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da lista é obrigatório.");
        }

        // Gera um novo UUID se for um novo registro
        if (lista.getId() == null) {
            lista.setId(UUID.randomUUID());
        }

        return repository.save(lista);
    }
}