package com.projetotematico1.notesflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projetotematico1.notesflow.model.entities.Lista;
import com.projetotematico1.notesflow.service.ListaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListaFormController implements Initializable {

    @FXML private TextField txtNomeLista;
    @FXML private ComboBox<Integer> comboNumColunas;
    @FXML private VBox vboxCamposContainer;

    private final ListaService listaService = new ListaService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Lista para rastrear os campos de texto gerados dinamicamente
    private final List<TextField> campoNomeTextFields = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Preenche o ComboBox com o número de colunas possíveis
        comboNumColunas.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7));
        comboNumColunas.getSelectionModel().select(3); // Seleciona 3 como padrão
        handleNumColunasChange(); // Inicializa com 3 campos
    }

    @FXML
    public void handleNumColunasChange() {
        int numColunas = comboNumColunas.getValue() != null ? comboNumColunas.getValue() : 3;

        // Remove todos os campos dinâmicos anteriores, mas mantém o Label inicial.
        vboxCamposContainer.getChildren().remove(1, vboxCamposContainer.getChildren().size());
        campoNomeTextFields.clear();

        for (int i = 0; i < numColunas; i++) {
            HBox fieldRow = new HBox(10);
            fieldRow.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Nome da Coluna " + (i + 1) + ":");
            TextField textField = new TextField();
            textField.setPromptText("Ex: Nome do Item");
            textField.setPrefWidth(200);

            fieldRow.getChildren().addAll(label, textField);
            vboxCamposContainer.getChildren().add(fieldRow);
            campoNomeTextFields.add(textField);
        }
    }

    @FXML
    public void salvarLista() {
        String nomeLista = txtNomeLista.getText();

        if (nomeLista == null || nomeLista.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "O nome da lista não pode ser vazio.");
            return;
        }

        // 1. Constrói o JsonNode dos campos personalizados
        ObjectNode camposNode = objectMapper.createObjectNode();
        boolean hasEmptyField = false;

        for (TextField tf : campoNomeTextFields) {
            String campoNome = tf.getText().trim();
            if (campoNome.isEmpty()) {
                hasEmptyField = true;
                break;
            }
            // A chave é o nome do campo e o valor é o tipo (simplificado para 'String')
            camposNode.put(campoNome, "String");
        }

        if (hasEmptyField) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Todos os nomes de colunas devem ser preenchidos.");
            return;
        }

        // 2. Cria a entidade Lista
        Lista novaLista = new Lista(null, nomeLista, camposNode);

        // 3. Salva no banco de dados
        try {
            listaService.save(novaLista);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Lista criada com sucesso!");
            fecharJanela();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao salvar a lista: " + e.getMessage());
        }
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        // Assume que qualquer campo é suficiente para obter a Stage
        Stage stage = (Stage) txtNomeLista.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}