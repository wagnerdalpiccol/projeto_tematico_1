package com.projetotematico1.notesflow.model.repository;

import com.projetotematico1.notesflow.db.DatabaseConnection;
import com.projetotematico1.notesflow.model.entities.Prioridade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrioridadeRepository {

    // 1. SELECT ALL
    public List<Prioridade> findAll() {
        List<Prioridade> prioridades = new ArrayList<>();
        String sql = "SELECT id, descricao FROM prioridades";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Conversão do UUID do banco para o objeto Java
                UUID id = rs.getObject("id", UUID.class);
                String descricao = rs.getString("descricao");
                prioridades.add(new Prioridade(id, descricao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar prioridades: " + e.getMessage());
            throw new RuntimeException("Falha na comunicação com o banco de dados.", e);
        }
        return prioridades;
    }

    public Prioridade save(Prioridade prioridade) {
        if (prioridade.getId() == null) {
            return insert(prioridade);
        } else {
            return update(prioridade);
        }
    }

    private Prioridade insert(Prioridade prioridade) {
        String sql = "INSERT INTO prioridades (id, descricao) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, prioridade.getId());
            pstmt.setString(2, prioridade.getDescricao());

            pstmt.executeUpdate();
            return prioridade;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir prioridade: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir prioridade no banco de dados.", e);
        }
    }

    private Prioridade update(Prioridade prioridade) {
        String sql = "UPDATE prioridades SET descricao = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prioridade.getDescricao());
            pstmt.setObject(2, prioridade.getId());

            pstmt.executeUpdate();
            return prioridade;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar prioridade: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar prioridade no banco de dados.", e);
        }
    }

    // 3. DELETE
    public void delete(UUID id) {
        String sql = "DELETE FROM prioridades WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar prioridade: " + e.getMessage());
            throw new RuntimeException("Não é possível deletar. Verifique se há tarefas usando esta prioridade.", e);
        }
    }
}
