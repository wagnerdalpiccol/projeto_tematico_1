package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Tarefa;
import com.projetotematico1.notesflow.service.TarefaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class TarefaController implements Initializable {

    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, String> colunaDescricao;
    @FXML private TableColumn<Tarefa, Integer> colunaStatus;
    @FXML private TableColumn<Tarefa, LocalDate> colunaDataInicio;
    @FXML private TableColumn<Tarefa, LocalDate> colunaDataFim;
    @FXML private TableColumn<Tarefa, Integer> colunaPrioridade;

    @FXML private TextField txtDescricao;
    @FXML private ComboBox<Integer> comboStatus;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataFim;
    @FXML private ComboBox<Integer> comboPrioridade;

    private TarefaService tarefaService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tarefaService = new TarefaService();
        configurarTabela();
        carregarTarefas();
    }

    private void configurarTabela() {
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colunaDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        colunaPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
    }

    private void carregarTarefas() {
        List<Tarefa> tarefas = tarefaService.findAll();
        tabelaTarefas.getItems().setAll(tarefas);
    }

    @FXML
    public void abrirNovaAbaAdicionarTarefa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projetotematico1/notesflow/view/tarefa-form-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Nova Tarefa");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recarrega a tabela após o fechamento da janela de adição, garantindo que
            // a nova tarefa seja exibida.
            carregarTarefas();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de adição de tarefas.");
        }
    }

    @FXML
    public void criarTarefa() {
        if (txtDescricao.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "A descrição não pode ser vazia.");
            return;
        }

        Tarefa novaTarefa = new Tarefa(
                txtDescricao.getText(),
                comboStatus.getValue(),
                dpDataInicio.getValue(),
                dpDataFim.getValue(),
                comboPrioridade.getValue()
        );

        tarefaService.save(novaTarefa);
        carregarTarefas();
        limparCampos();
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa criada com sucesso!");
    }

    @FXML
    public void atualizarTarefa() {
        Tarefa tarefaSelecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (tarefaSelecionada != null) {
            tarefaSelecionada.setDescricao(txtDescricao.getText());
            tarefaSelecionada.setStatus(comboStatus.getValue());
            tarefaSelecionada.setDataInicio(dpDataInicio.getValue());
            tarefaSelecionada.setDataFim(dpDataFim.getValue());
            tarefaSelecionada.setPrioridade(comboPrioridade.getValue());

            tarefaService.update(tarefaSelecionada);
            carregarTarefas();
            limparCampos();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa atualizada!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma tarefa para atualizar.");
        }
    }

    @FXML
    public void deletarTarefa() {
        Tarefa tarefaSelecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (tarefaSelecionada != null) {
            tarefaService.delete(tarefaSelecionada.getId());
            carregarTarefas();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa excluída!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma tarefa para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtDescricao.clear();
        comboStatus.getSelectionModel().clearSelection();
        dpDataInicio.setValue(null);
        dpDataFim.setValue(null);
        comboPrioridade.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}