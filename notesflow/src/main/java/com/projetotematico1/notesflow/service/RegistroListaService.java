package com.projetotematico1.notesflow.service;

import com.projetotematico1.notesflow.model.entities.RegistroLista;
import com.projetotematico1.notesflow.model.repository.RegistroListaRepository;

import java.util.List;
import java.util.UUID;

public class RegistroListaService {

    private final RegistroListaRepository repository = new RegistroListaRepository();

    public List<RegistroLista> findByListaId(UUID listaId) {
        return repository.findByListaId(listaId);
    }

    public RegistroLista save(RegistroLista registro) {
        if (registro.getListaId() == null) {
            throw new IllegalArgumentException("O registro deve estar associado a uma Lista.");
        }

        // Se for um novo registro, gera um novo UUID
        /*if (registro.getId() == null) {
            registro.setId(UUID.randomUUID());
        }*/

        return repository.save(registro);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}