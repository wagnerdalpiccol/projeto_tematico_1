package com.projetotematico1.notesflow;
import com.projetotematico1.notesflow.model.enums.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NavigationManager {

    public static void navigateTo(ActionEvent event, Screen screen) {
        try {
            // Obtém o Stage a partir do evento dentro da classe de navegação
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Carrega o FXML e define a nova cena
            Parent root = FXMLLoader.load(NavigationManager.class.getResource(screen.getFxmlPath()));
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + screen.getFxmlPath());
        }
    }

    public static void navigateTo(Stage stage, Screen screen) {
        try {
            Parent root = FXMLLoader.load(NavigationManager.class.getResource(screen.getFxmlPath()));
            Scene scene = new Scene(root, 1000, 600);
            Image icon = new Image(Objects.requireNonNull(NavigationManager.class.getResourceAsStream("/com/projetotematico1/notesflow/images/notes_flow_logo_mini.png")));
            stage.setTitle("NotesFlow");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + screen.getFxmlPath());
        }
    }
}