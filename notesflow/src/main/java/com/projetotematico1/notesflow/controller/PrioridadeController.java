package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Prioridade;
import com.projetotematico1.notesflow.service.PrioridadeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrioridadeController implements Initializable {


    @FXML private TableView<Prioridade> tabelaPrioridades;
    @FXML private TableColumn<Prioridade, String> colunaDescricao;
    @FXML private TextField txtDescricao;
    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;


    // é para o Controller chamar apenas o Service, para manter a arquitetura limpa
    private final PrioridadeService prioridadeService = new PrioridadeService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabela();
        carregarPrioridades();

        // Configura o evento para carregar a prioridade no campo de texto ao ser selecionada
        tabelaPrioridades.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        txtDescricao.setText(newValue.getDescricao());
                        btnSalvar.setText("Atualizar");
                    } else {
                        limparCampos();
                        btnSalvar.setText("Salvar");
                    }
                }
        );
    }

    private void configurarTabela() {
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }

    private void carregarPrioridades() {
        try {
            // Chama o Service para buscar todos os registros no PostgreSQL
            List<Prioridade> prioridades = prioridadeService.findAll();
            ObservableList<Prioridade> observableList = FXCollections.observableArrayList(prioridades);
            tabelaPrioridades.setItems(observableList);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível carregar as prioridades. Verifique o banco de dados.");
            // Log do erro
            System.err.println("Erro ao carregar prioridades: " + e.getMessage());
        }
    }

    @FXML
    public void salvarPrioridade() {
        String descricao = txtDescricao.getText();
        Prioridade prioridadeSelecionada = tabelaPrioridades.getSelectionModel().getSelectedItem();

        if (descricao.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "A descrição não pode estar vazia.");
            return;
        }

        try {
            if (prioridadeSelecionada == null) {
                // Criação (o Service gera o UUID)
                Prioridade novaPrioridade = new Prioridade(null, descricao);
                prioridadeService.save(novaPrioridade);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Prioridade criada!");
            } else {
                // Atualização
                prioridadeSelecionada.setDescricao(descricao);
                prioridadeService.update(prioridadeSelecionada);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Prioridade atualizada!");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao salvar a prioridade.");
        }

        carregarPrioridades();
        limparCampos();
    }

    @FXML
    public void deletarPrioridade() {
        Prioridade prioridadeSelecionada = tabelaPrioridades.getSelectionModel().getSelectedItem();

        if (prioridadeSelecionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Excluir Prioridade");
            alert.setContentText("Tem certeza que deseja excluir: " + prioridadeSelecionada.getDescricao() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    prioridadeService.delete(prioridadeSelecionada.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Prioridade excluída!");
                } catch (RuntimeException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao excluir. Verifique se há tarefas associadas que dependem desta prioridade.");
                }
                carregarPrioridades();
                limparCampos();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma prioridade para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtDescricao.clear();
        tabelaPrioridades.getSelectionModel().clearSelection();
        btnSalvar.setText("Salvar");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}