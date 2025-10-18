package com.projetotematico1.notesflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.projetotematico1.notesflow.model.entities.Lista;
import com.projetotematico1.notesflow.model.entities.RegistroLista;
import com.projetotematico1.notesflow.model.enums.Screen;
import com.projetotematico1.notesflow.service.RegistroListaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistroListaController implements Initializable {

    @FXML private Label lblNomeLista;
    @FXML private TableView<RegistroLista> tabelaRegistros;
    @FXML private Button btnAdicionar;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Lista lista;
    private final RegistroListaService registroService = new RegistroListaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        tabelaRegistros.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            boolean isSelected = newValue != null;
            btnEditar.setDisable(!isSelected);
            btnExcluir.setDisable(!isSelected);
        });
    }

    public void setLista(Lista lista) {
        this.lista = lista;
        lblNomeLista.setText("Registros: " + lista.getNomelista());
        configurarTabelaDinamica();
        carregarRegistros();
    }

    private void configurarTabelaDinamica() {
        tabelaRegistros.getColumns().clear();

        Iterator<String> fieldNames = lista.getCampos().fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            TableColumn<RegistroLista, String> column = new TableColumn<>(fieldName);

            column.setCellValueFactory(cellData -> {
                JsonNode dados = cellData.getValue().getDados();
                String value = dados.has(fieldName) ? dados.get(fieldName).asText() : "";
                return new SimpleStringProperty(value);
            });

            tabelaRegistros.getColumns().add(column);
        }
    }

    // Corrigido: Método público para ser usado como Runnable callback
    public void carregarRegistros() {
        try {
            List<RegistroLista> registros = registroService.findByListaId(lista.getId());
            tabelaRegistros.getItems().setAll(registros);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar os registros: " + e.getMessage());
        }
    }

    @FXML
    public void adicionarRegistro() {
        abrirFormularioRegistro(null);
    }

    @FXML
    public void editarRegistro() {
        RegistroLista registroSelecionado = tabelaRegistros.getSelectionModel().getSelectedItem();
        if (registroSelecionado != null) {
            abrirFormularioRegistro(registroSelecionado);
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione um registro para editar.");
        }
    }

    private void abrirFormularioRegistro(RegistroLista registro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.REGISTRO_FORM.getFxmlPath()));
            Parent root = loader.load();

            RegistroFormController formController = loader.getController();
            // Corrigido: Passando this::carregarRegistros como Runnable
            formController.initData(lista, registro, this::carregarRegistros);

            Stage stage = new Stage();
            stage.setTitle(registro == null ? "Novo Registro" : "Editar Registro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar o formulário de registro.");
        }
    }

    @FXML
    public void deletarRegistro() {
        RegistroLista registroSelecionado = tabelaRegistros.getSelectionModel().getSelectedItem();
        if (registroSelecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Excluir Registro");
            alert.setContentText("Tem certeza que deseja excluir este registro?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    registroService.delete(registroSelecionado.getId());
                    carregarRegistros();
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Registro excluído!");
                } catch (RuntimeException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao excluir o registro: " + e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione um registro para excluir.");
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