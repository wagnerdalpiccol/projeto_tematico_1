package com.projetotematico1.notesflow.model.enums;

public enum Screen {
    LOGIN("/com/projetotematico1/notesflow/view/main-view.fxml"),
    MANAGEMENT("/com/projetotematico1/notesflow/view/management-view.fxml");

    private final String fxmlPath;

    Screen(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
