package com.projetotematico1.notesflow.model.entities;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;

public class Lista {

    private UUID id;
    private String nomelista;
    private JsonNode campos;

    public Lista() {
    }

    public Lista(UUID id, String nomelista, JsonNode campos) {
        this.id = id;
        this.nomelista = nomelista;
        this.campos = campos;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomelista() {
        return nomelista;
    }

    public void setNomelista(String nomelista) {
        this.nomelista = nomelista;
    }

    public JsonNode getCampos() {
        return campos;
    }

    public void setCampos(JsonNode campos) {
        this.campos = campos;
    }
}