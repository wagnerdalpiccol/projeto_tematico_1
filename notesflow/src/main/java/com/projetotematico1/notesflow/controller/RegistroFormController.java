package com.projetotematico1.notesflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projetotematico1.notesflow.model.entities.Lista;
import com.projetotematico1.notesflow.model.entities.RegistroLista;
import com.projetotematico1.notesflow.service.RegistroListaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class RegistroFormController implements Initializable {

    @FXML private GridPane gridCampos;
    private final RegistroListaService registroService = new RegistroListaService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Lista lista;
    private RegistroLista registroEmEdicao;
    private Runnable onSaveCallback; // Corrigido: Usando Runnable para callbacks sem argumento

    private final Map<String, TextField> campoInputs = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridCampos.setHgap(10);
        gridCampos.setVgap(10);
        gridCampos.setPadding(new Insets(20));
    }

    // Corrigido: Assinatura agora recebe um Runnable
    public void initData(Lista lista, RegistroLista registro, Runnable onSaveCallback) {
        this.lista = lista;
        this.registroEmEdicao = registro;
        this.onSaveCallback = onSaveCallback;

        gerarCamposFormulario();

        if (registroEmEdicao != null) {
            carregarDadosParaEdicao();
        }
    }

    private void gerarCamposFormulario() {
        Iterator<String> fieldNames = lista.getCampos().fieldNames();
        int row = 0;

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            Label label = new Label(fieldName + ":");
            TextField textField = new TextField();
            textField.setPromptText("Digite o valor para " + fieldName);

            gridCampos.add(label, 0, row);
            gridCampos.add(textField, 1, row);

            campoInputs.put(fieldName, textField);
            row++;
        }
    }

    private void carregarDadosParaEdicao() {
        if (registroEmEdicao != null && registroEmEdicao.getDados() != null) {
            registroEmEdicao.getDados().fieldNames().forEachRemaining(fieldName -> {
                if (campoInputs.containsKey(fieldName)) {
                    // Preenche o campo com o valor salvo
                    campoInputs.get(fieldName).setText(registroEmEdicao.getDados().get(fieldName).asText());
                }
            });
        }
    }

    @FXML
    public void salvarRegistro() {
        ObjectNode dadosNode = objectMapper.createObjectNode();
        boolean hasEmptyField = false;

        for (Map.Entry<String, TextField> entry : campoInputs.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().getText();

            if (fieldValue.trim().isEmpty()) {
                hasEmptyField = true;
                break;
            }
            dadosNode.put(fieldName, fieldValue);
        }

        if (hasEmptyField) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Todos os campos devem ser preenchidos.");
            return;
        }

        // Criação ou Edição
        if (registroEmEdicao == null) {
            RegistroLista novoRegistro = new RegistroLista();
            novoRegistro.setListaId(lista.getId());
            novoRegistro.setDados(dadosNode);
            registroService.save(novoRegistro);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Registro criado!");
        } else {
            registroEmEdicao.setDados(dadosNode);
            registroService.save(registroEmEdicao);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Registro atualizado!");
        }

        if (onSaveCallback != null) {
            onSaveCallback.run(); // Corrigido: Chamando o método run() do Runnable
        }
        fecharJanela();
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) gridCampos.getScene().getWindow();
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