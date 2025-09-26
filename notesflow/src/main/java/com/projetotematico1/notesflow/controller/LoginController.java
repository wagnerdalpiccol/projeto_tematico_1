package com.projetotematico1.notesflow.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usuarioTextField;

    @FXML
    private PasswordField senhaPasswordField;

    @FXML
    private void handleEntrarButtonAction(ActionEvent event) {
        String usuario = usuarioTextField.getText();
        String senha = senhaPasswordField.getText();

        // 1. Verificação de usuário e senha
        if (usuario.equals("admin") && senha.equals("123")) {
            System.out.println("Login bem-sucedido!");

            // 2. Navegar para a nova tela (management-view.fxml)
            try {
                // Carrega a nova tela a partir do arquivo FXML
                Parent managementViewParent = FXMLLoader.load(getClass().getResource("management-view.fxml"));
                Scene managementViewScene = new Scene(managementViewParent);

                // Obtém a janela (Stage) atual a partir do botão clicado
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                // Define a nova cena na janela e exibe
                window.setScene(managementViewScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usuário ou senha inválidos!");
            // Você pode adicionar um alerta ou uma label para mostrar a mensagem de erro na tela
        }
    }
}
