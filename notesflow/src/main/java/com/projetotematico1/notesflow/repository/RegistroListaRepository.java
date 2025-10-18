package com.projetotematico1.notesflow.model.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetotematico1.notesflow.db.DatabaseConnection;
import com.projetotematico1.notesflow.model.entities.RegistroLista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistroListaRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ----------------------------------------------------------------------
    // FIND
    // ----------------------------------------------------------------------
    public List<RegistroLista> findByListaId(UUID listaId) {
        List<RegistroLista> registros = new ArrayList<>();
        String sql = "SELECT id, listaId, dados FROM registros_lista WHERE listaId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, listaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = rs.getObject("id", UUID.class);
                    UUID parentListaId = rs.getObject("listaId", UUID.class);
                    String jsonDados = rs.getString("dados");

                    JsonNode dados = objectMapper.readTree(jsonDados != null ? jsonDados : "{}");

                    registros.add(new RegistroLista(id, parentListaId, dados));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar registros da lista: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha na comunicação com o banco de dados.", e);
        } catch (Exception e) {
            System.err.println("Erro de processamento JSON: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro de processamento JSON.", e);
        }
        return registros;
    }

    public RegistroLista save(RegistroLista registro) {
        if (registro.getId() == null) {
            return insert(registro);
        } else {
            return update(registro);
        }
    }

    // ----------------------------------------------------------------------
    // INSERT (Corrigido: Adicionado ::jsonb)
    // ----------------------------------------------------------------------
    private RegistroLista insert(RegistroLista registro) {
        // A CONVERSÃO EXPLÍCITA ::jsonb resolve o problema de tipo
        String sql = "INSERT INTO registros_lista (id, listaId, dados) VALUES (?, ?, ?::jsonb)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);

            pstmt.setObject(1, registro.getId());
            pstmt.setObject(2, registro.getListaId());

            String jsonDados = objectMapper.writeValueAsString(registro.getDados());
            // Voltamos a usar setString, o cast no SQL resolve a incompatibilidade
            pstmt.setString(3, jsonDados);

            pstmt.executeUpdate();

            return registro;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir registro no banco de dados.", e);
        } catch (Exception e) {
            System.err.println("Erro de serialização/deserialização: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro de processamento JSON.", e);
        }
    }

    // ----------------------------------------------------------------------
    // UPDATE (Corrigido: Adicionado ::jsonb)
    // ----------------------------------------------------------------------
    private RegistroLista update(RegistroLista registro) {
        // A CONVERSÃO EXPLÍCITA ::jsonb resolve o problema de tipo
        String sql = "UPDATE registros_lista SET dados = ?::jsonb WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);

            String jsonDados = objectMapper.writeValueAsString(registro.getDados());
            // Voltamos a usar setString, o cast no SQL resolve a incompatibilidade
            pstmt.setString(1, jsonDados);
            pstmt.setObject(2, registro.getId());

            pstmt.executeUpdate();
            return registro;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar registro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar registro no banco de dados.", e);
        } catch (Exception e) {
            System.err.println("Erro de serialização/deserialização: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro de processamento JSON.", e);
        }
    }

    // ----------------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------------
    public void delete(UUID id) {
        String sql = "DELETE FROM registros_lista WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);

            pstmt.setObject(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar registro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar registro.", e);
        }
    }
}