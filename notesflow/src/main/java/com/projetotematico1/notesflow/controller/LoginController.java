package com.projetotematico1.notesflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import com.projetotematico1.notesflow.NavigationManager;
import com.projetotematico1.notesflow.model.enums.Screen;

public class LoginController {

    @FXML
    private TextField usuarioTextField;

    @FXML
    private PasswordField senhaPasswordField;

    @FXML
    private void handleEntrarButtonAction(ActionEvent event) {
        String usuario = usuarioTextField.getText();
        String senha = senhaPasswordField.getText();

        if (usuario.equals("admin") && senha.equals("123")) {
            System.out.println("Login bem-sucedido!");
            NavigationManager.navigateTo(event, Screen.TAREFA);

        } else {
            System.out.println("Usuário ou senha inválidos!");
        }
    }
}