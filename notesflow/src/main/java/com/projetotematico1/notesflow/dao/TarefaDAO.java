package com.projetotematico1.notesflow.dao;

import com.projetotematico1.notesflow.model.Tarefa;
import com.projetotematico1.notesflow.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TarefaDAO {

    // A lista em memória não é mais necessária, pois o DAO se comunica com o banco de dados.
    // private final List<Tarefa> tarefas = new ArrayList<>();

    // Método para adicionar uma nova tarefa no banco de dados
    public void adicionar(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas (id, descricao, status, datainicio, datafim, prioridade) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, tarefa.getId());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getStatus());

            stmt.setDate(4, Date.valueOf(tarefa.getDataInicio()));
            stmt.setDate(5, Date.valueOf(tarefa.getDataFim()));

            stmt.setInt(6, tarefa.getPrioridade());

            stmt.executeUpdate();
            System.out.println("Tarefa adicionada com sucesso no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar a tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para buscar todas as tarefas do banco de dados
    public List<Tarefa> getAll() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT id, descricao, status, datainicio, datafim, prioridade FROM tarefas";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Usamos executeQuery para comandos SELECT

            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(UUID.fromString(rs.getString("id")));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setStatus(rs.getInt("status"));
                tarefa.setDataInicio(rs.getDate("datainicio").toLocalDate());
                tarefa.setDataFim(rs.getDate("datafim").toLocalDate());
                tarefa.setPrioridade(rs.getInt("prioridade"));

                tarefas.add(tarefa);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }

    // Método para atualizar uma tarefa existente no banco de dados
    public void atualizar(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET descricao = ?, status = ?, datainicio = ?, datafim = ?, prioridade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getDescricao());
            stmt.setInt(2, tarefa.getStatus());
            stmt.setDate(3, Date.valueOf(tarefa.getDataInicio()));
            stmt.setDate(4, Date.valueOf(tarefa.getDataFim()));
            stmt.setInt(5, tarefa.getPrioridade());
            stmt.setObject(6, tarefa.getId()); // O ID da tarefa a ser atualizada

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tarefa atualizada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada para atualização com o ID: " + tarefa.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para deletar uma tarefa do banco de dados pelo ID
    public void deletar(UUID id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tarefa deletada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada para exclusão com o ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar a tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}