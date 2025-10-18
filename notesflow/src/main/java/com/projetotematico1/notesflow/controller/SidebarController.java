package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.NavigationManager;
import com.projetotematico1.notesflow.model.enums.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SidebarController {

    @FXML
    public void goToTarefas(ActionEvent event) {
        NavigationManager.navigateTo(event, Screen.TAREFA);
    }

    @FXML
    public void goToQuadro(ActionEvent event) {
        NavigationManager.navigateTo(event, Screen.QUADRO);
    }

    @FXML
    public void goToListas(ActionEvent event) {
        NavigationManager.navigateTo(event, Screen.LISTA);
    }
}