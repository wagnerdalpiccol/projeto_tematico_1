package com.projetotematico1.notesflow;

import com.projetotematico1.notesflow.repository.TarefaRepository;
import com.projetotematico1.notesflow.entity.Tarefa;
import com.projetotematico1.notesflow.service.TarefaService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class HelloController {

    // Injeção dos elementos do FXML (fx:id)
    @FXML
    private TextField descricaoTextField;
    @FXML
    private DatePicker dataIniDatePicker;
    @FXML
    private DatePicker dataFimDatePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> prioridadeComboBox;
    @FXML
    private Label statusLabel;

    // Declaração dos objetos Service e DAO
    private TarefaRepository tarefaRepository;
    private TarefaService tarefaService;

    // Método que é chamado automaticamente após o FXML ser carregado
    @FXML
    public void initialize() {
        // Inicializa os objetos Service e DAO
        this.tarefaRepository = new TarefaRepository();
        this.tarefaService = new TarefaService(tarefaRepository);
    }

    // Método que será executado ao clicar no botão "Salvar"
    @FXML
    protected void onSalvarButtonClick() {
        // Coletando os dados dos controles da interface
        String descricao = descricaoTextField.getText();
        LocalDate dataIni = dataIniDatePicker.getValue();
        LocalDate dataFim = dataFimDatePicker.getValue();
        String status = statusComboBox.getValue();
        String prioridade = prioridadeComboBox.getValue();

        Tarefa novaTarefa = new Tarefa(descricao, 1, dataIni, dataFim, 2);

        tarefaService.adicionar(novaTarefa);

        // Limpando os campos após o salvamento
        descricaoTextField.clear();
        dataIniDatePicker.setValue(null);
        dataFimDatePicker.setValue(null);
        statusComboBox.setValue(null);
        prioridadeComboBox.setValue(null);

        // Exibindo uma mensagem de sucesso na interface
        statusLabel.setText("Tarefa salva com sucesso!");
    }
}