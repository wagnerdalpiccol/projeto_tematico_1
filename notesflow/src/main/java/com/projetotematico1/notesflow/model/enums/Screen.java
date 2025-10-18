// notesflow/src/main/java/com/projetotematico1/notesflow/model/enums/Screen.java

package com.projetotematico1.notesflow.model.enums;

public enum Screen {
    LOGIN("/com/projetotematico1/notesflow/view/tela-inicial-view.fxml"),
    MANAGEMENT("/com/projetotematico1/notesflow/view/tela-padrao.fxml"),
    TAREFA("/com/projetotematico1/notesflow/view/tarefas-view.fxml"),
    QUADRO("/com/projetotematico1/notesflow/view/quadro-kanban-view.fxml"), // Novo: Quadro Kanban
    LISTA("/com/projetotematico1/notesflow/view/listas-view.fxml"); // Novo: Para o botão "Listas"

    private final String fxmlPath;

    Screen(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}