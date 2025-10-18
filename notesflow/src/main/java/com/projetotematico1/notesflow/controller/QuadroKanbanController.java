package com.projetotematico1.notesflow.controller;

import com.projetotematico1.notesflow.model.entities.Tarefa;
import com.projetotematico1.notesflow.service.TarefaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QuadroKanbanController implements Initializable {

    @FXML private VBox colunaNaoIniciada;
    @FXML private VBox colunaEmAndamento;
    @FXML private VBox colunaConcluida;

    // Injetando dependência do serviço de tarefas
    private final TarefaService tarefaService = new TarefaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // As colunas já contêm o Label do título (index 0).
        // A partir do index 1, o método carregarTarefasNoQuadro irá adicionar os cards.
        carregarTarefasNoQuadro();
    }

    private void carregarTarefasNoQuadro() {
        // Mantém apenas a Label do título em cada VBox, removendo os cards anteriores.
        colunaNaoIniciada.getChildren().remove(1, colunaNaoIniciada.getChildren().size());
        colunaEmAndamento.getChildren().remove(1, colunaEmAndamento.getChildren().size());
        colunaConcluida.getChildren().remove(1, colunaConcluida.getChildren().size());

        List<Tarefa> tarefas = tarefaService.findAll();

        for (Tarefa tarefa : tarefas) {
            VBox card = criarCardTarefa(tarefa);
            switch (tarefa.getStatus()) {
                case 0: // Não Iniciada
                    colunaNaoIniciada.getChildren().add(card);
                    break;
                case 1: // Em Andamento
                    colunaEmAndamento.getChildren().add(card);
                    break;
                case 2: // Concluída
                    colunaConcluida.getChildren().add(card);
                    break;
                default:
                    // Tarefas com status desconhecido (ex: null ou > 2) não são exibidas.
                    break;
            }
        }
    }

    /**
     * Cria um VBox estilizado para representar o card de uma Tarefa.
     * @param tarefa A Tarefa a ser exibida.
     * @return O VBox que representa o card.
     */
    private VBox criarCardTarefa(Tarefa tarefa) {
        VBox card = new VBox(5);
        // Estilização do card Kanban
        card.setStyle("-fx-background-color: white; -fx-border-color: #999999; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4;");
        card.setPadding(new Insets(10));
        VBox.setMargin(card, new Insets(0, 10, 10, 10)); // Margem para separar os cards

        Label descricaoLabel = new Label(tarefa.getDescricao());
        descricaoLabel.setStyle("-fx-font-weight: bold; -fx-wrap-text: true;");

        // Exibe detalhes da prioridade
        Label prioridadeLabel = new Label("Prioridade: " + tarefa.getPrioridade());

        card.getChildren().addAll(descricaoLabel, prioridadeLabel);
        return card;
    }
}