package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Lista;
import com.projetotematico1.notesflow.model.enums.Screen;
import com.projetotematico1.notesflow.service.ListaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListaController implements Initializable {

    @FXML private VBox vboxListasContainer;

    private final ListaService listaService = new ListaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarListas();
    }

    private void carregarListas() {
        vboxListasContainer.getChildren().clear();

        try {
            List<Lista> listas = listaService.findAll();

            if (listas.isEmpty()) {
                vboxListasContainer.getChildren().add(new Label("Nenhuma lista encontrada. Clique em 'Criar Nova Lista'."));
            } else {
                for (Lista lista : listas) {
                    VBox listaView = criarListaView(lista);
                    vboxListasContainer.getChildren().add(listaView);
                }
            }
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar listas: " + e.getMessage());
        }
    }

    private VBox criarListaView(Lista lista) {
        VBox view = new VBox(5);
        view.setPadding(new Insets(15));
        view.setStyle("-fx-border-color: #0077b6; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: #e6f7ff;");
        view.setMaxWidth(Double.MAX_VALUE);

        Label nomeLabel = new Label("Lista: " + lista.getNomelista());
        nomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        StringBuilder colunas = new StringBuilder();
        lista.getCampos().fieldNames().forEachRemaining(name -> {
            if (colunas.length() > 0) colunas.append(", ");
            colunas.append(name);
        });
        Label nomesColunasLabel = new Label("Campos: " + colunas.toString());
        nomesColunasLabel.setWrapText(true);

        // Botão para ver registros
        Button btnVerRegistros = new Button("Ver Registros / Adicionar Dados");
        btnVerRegistros.setOnAction(event -> {
            abrirTelaRegistros(lista);
        });

        HBox actions = new HBox(10, btnVerRegistros);
        view.getChildren().addAll(nomeLabel, nomesColunasLabel, actions);
        return view;
    }

    @FXML
    public void abrirFormularioCriacaoLista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projetotematico1/notesflow/view/lista-form-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Criar Nova Lista");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarListas();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de criação de lista.");
        }
    }

    private void abrirTelaRegistros(Lista lista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.REGISTRO_LISTA.getFxmlPath()));
            Parent root = loader.load();

            RegistroListaController controller = loader.getController();
            controller.setLista(lista); // Passa a Lista para o controller de Registros

            Stage stage = new Stage();
            stage.setTitle("Registros da Lista: " + lista.getNomelista());
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de registros da lista.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}