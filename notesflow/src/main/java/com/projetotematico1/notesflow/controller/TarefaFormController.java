package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Tarefa;
import com.projetotematico1.notesflow.service.TarefaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TarefaFormController implements Initializable {

    @FXML private TextField txtDescricao;
    @FXML private ComboBox<Integer> comboStatus;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataFim;
    @FXML private ComboBox<Integer> comboPrioridade;

    private TarefaService tarefaService;
    private Tarefa tarefaEmEdicao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tarefaService = new TarefaService();
        inicializarCombos();
    }

    private void inicializarCombos() {
        comboStatus.getItems().addAll(0, 1); // 0 = A Fazer, 1 = Concluído
        comboPrioridade.getItems().addAll(1, 2, 3, 4, 5); // 1 = Baixa, 5 = Alta
    }

    // Método chamado pelo controlador pai (TarefaController) para carregar dados
    public void setTarefaParaEdicao(Tarefa tarefa) {
        this.tarefaEmEdicao = tarefa;
        if (tarefaEmEdicao != null) {
            txtDescricao.setText(tarefa.getDescricao());
            comboStatus.getSelectionModel().select(tarefa.getStatus());
            dpDataInicio.setValue(tarefa.getDataInicio());
            dpDataFim.setValue(tarefa.getDataFim());
            comboPrioridade.getSelectionModel().select(tarefa.getPrioridade());
        }
    }

    @FXML
    public void salvarTarefa() {
        if (txtDescricao.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "A descrição não pode ser vazia.");
            return;
        }

        if (tarefaEmEdicao == null) {
            // Lógica para criar uma nova tarefa
            Tarefa novaTarefa = new Tarefa(
                    txtDescricao.getText(),
                    comboStatus.getValue(),
                    dpDataInicio.getValue(),
                    dpDataFim.getValue(),
                    comboPrioridade.getValue()
            );
            tarefaService.save(novaTarefa);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa criada com sucesso!");
        } else {
            // Lógica para atualizar uma tarefa existente
            tarefaEmEdicao.setDescricao(txtDescricao.getText());
            tarefaEmEdicao.setStatus(comboStatus.getValue());
            tarefaEmEdicao.setDataInicio(dpDataInicio.getValue());
            tarefaEmEdicao.setDataFim(dpDataFim.getValue());
            tarefaEmEdicao.setPrioridade(comboPrioridade.getValue());
            tarefaService.update(tarefaEmEdicao);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tarefa atualizada!");
        }

        fecharJanela();
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtDescricao.getScene().getWindow();
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