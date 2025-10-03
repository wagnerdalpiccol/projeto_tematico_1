package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Tarefa;
import com.projetotematico1.notesflow.service.TarefaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class TarefaController implements Initializable {

    // --- Mapeamento dos elementos do FXML ---
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

    // --- Métodos de ciclo de vida do controller ---
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tarefaService = new TarefaService(); // Instancie seu serviço aqui
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
        //List<Tarefa> tarefas = tarefaService.findAll();
        //tabelaTarefas.getItems().setAll(tarefas);
    }

    // --- Métodos de CRUD (Chamados por eventos da UI) ---

    @FXML
    public void criarTarefa() {
        // Validação básica dos campos
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

        //tarefaService.save(novaTarefa);
        carregarTarefas(); // Atualiza a tabela
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

            //tarefaService.update(tarefaSelecionada.getId(), tarefaSelecionada);
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
            //tarefaService.delete(tarefaSelecionada.getId());
            carregarTarefas();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa excluída!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma tarefa para excluir.");
        }
    }

    // --- Métodos de utilidade para a UI ---

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