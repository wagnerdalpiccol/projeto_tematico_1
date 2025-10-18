// notesflow/src/main/java/com/projetotematico1/notesflow/model/entities/RegistroLista.java
package com.projetotematico1.notesflow.model.entities;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;

public class RegistroLista {

    private UUID id;
    private UUID listaId; // Chave estrangeira para a Lista pai
    private JsonNode dados; // Os dados reais do registro (Custom Fields)

    public RegistroLista() {
    }

    public RegistroLista(UUID id, UUID listaId, JsonNode dados) {
        this.id = id;
        this.listaId = listaId;
        this.dados = dados;
    }

    // Getters and Setters (omiti para brevidade)
    // ...
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getListaId() {
        return listaId;
    }
    public void setListaId(UUID listaId) {
        this.listaId = listaId;
    }
    public JsonNode getDados() {
        return dados;
    }
    public void setDados(JsonNode dados) {
        this.dados = dados;
    }
}