package com.projetotematico1.notesflow.model.enums;

public enum Screen {
    LOGIN("/com/projetotematico1/notesflow/view/tela-inicial-view.fxml"),
    MANAGEMENT("/com/projetotematico1/notesflow/view/tela-padrao.fxml"),
    TAREFA("/com/projetotematico1/notesflow/view/tarefas-view.fxml"),
    QUADRO("/com/projetotematico1/notesflow/view/quadro-kanban-view.fxml"),
    LISTA("/com/projetotematico1/notesflow/view/listas-view.fxml"),
    REGISTRO_LISTA("/com/projetotematico1/notesflow/view/registro-lista-view.fxml"), // Novo: Gerenciamento de Registros
    REGISTRO_FORM("/com/projetotematico1/notesflow/view/registro-form-view.fxml"); // Novo: Formulário de Registro

    private final String fxmlPath;

    Screen(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}