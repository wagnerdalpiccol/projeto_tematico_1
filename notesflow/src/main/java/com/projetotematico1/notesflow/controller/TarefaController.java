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
import java.util.ResourceBundle;

public class TarefaController implements Initializable {

    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, String> colunaDescricao;
    @FXML private TableColumn<Tarefa, Integer> colunaStatus;
    @FXML private TableColumn<Tarefa, LocalDate> colunaDataInicio;
    @FXML private TableColumn<Tarefa, LocalDate> colunaDataFim;
    @FXML private TableColumn<Tarefa, Integer> colunaPrioridade;

    // Campos FXML existentes (para a funcionalidade antiga de CRUD direta na tela principal,
    // mas que não serão usados para o contexto de edição via modal)
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

    // Método refatorado para usar o método auxiliar. Agora só chama a abertura com null (nova tarefa).
    @FXML
    public void abrirNovaAbaAdicionarTarefa() {
        abrirFormularioTarefa(null);
    }

    // NOVO MÉTODO: Lida com a ação de editar vinda do ContextMenu (clique direito)
    @FXML
    public void handleEditarTarefaContext() {
        Tarefa tarefaSelecionada = tabelaTarefas.getSelectionModel().getSelectedItem();

        if (tarefaSelecionada != null) {
            abrirFormularioTarefa(tarefaSelecionada); // Passa a tarefa para edição
        } else {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma tarefa para editar.");
        }
    }

    /**
     * Método auxiliar privado para abrir o formulário de tarefa em uma nova janela modal.
     * @param tarefa O objeto Tarefa a ser editado (ou null para uma nova tarefa).
     */
    private void abrirFormularioTarefa(Tarefa tarefa) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projetotematico1/notesflow/view/nova-tarefa-view.fxml"));
            Parent root = loader.load();

            // 1. Obtém a instância do controller do formulário.
            TarefaFormController formController = loader.getController();

            // 2. Se a tarefa não for nula, passa os dados para o formulário.
            if (tarefa != null) {
                formController.setTarefaParaEdicao(tarefa);
            }

            Stage stage = new Stage();
            // 3. Define o título com base na ação (Adicionar ou Editar).
            stage.setTitle(tarefa == null ? "Adicionar Nova Tarefa" : "Editar Tarefa");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recarrega a tabela principal após o formulário ser fechado.
            carregarTarefas();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de formulário de tarefas.");
        }
    }

    // Os métodos criarTarefa, atualizarTarefa, deletarTarefa e limparCampos originais foram mantidos.
    // Eles se referem aos controles de campo na própria tela de Tarefas, se for um formulário embutido.

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