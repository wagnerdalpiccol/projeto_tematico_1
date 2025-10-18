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
        // A tela LISTA será implementada posteriormente.
        // Por enquanto, podemos navegar para uma tela placeholder ou a tela padrão.
        // Assumindo que você criará Screen.LISTA:
        // NavigationManager.navigateTo(event, Screen.LISTA);

        // Usando a tela padrão por enquanto, ou você pode criar uma Screen.LISTA
        // e mapeá-la para um fxml de placeholder.
        System.out.println("Navegando para Listas (ainda não implementado: Screen.LISTA)");
        NavigationManager.navigateTo(event, Screen.MANAGEMENT);
    }
}