package com.projetotematico1.notesflow.model.entities;

import java.util.UUID;

public class Prioridade {

    private UUID id;
    private String descricao;

    public Prioridade() {
    }

    public Prioridade(UUID id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}