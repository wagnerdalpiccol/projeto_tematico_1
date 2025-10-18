package com.projetotematico1.notesflow.model.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetotematico1.notesflow.db.DatabaseConnection;
import com.projetotematico1.notesflow.model.entities.Lista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListaRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Lista save(Lista lista) {
        // Para simplificar, assumimos que todo save de lista é um novo INSERT.
        return insert(lista);
    }

    private Lista insert(Lista lista) {
        String sql = "INSERT INTO listas (id, nomelista, campos) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, lista.getId());
            pstmt.setString(2, lista.getNomelista());

            // Converte JsonNode para String (JSON) para inserção no PostgreSQL
            String jsonCampos = lista.getCampos() != null ? objectMapper.writeValueAsString(lista.getCampos()) : "{}";
            pstmt.setObject(3, jsonCampos, Types.OTHER); // Tipagem para JSON/JSONB

            pstmt.executeUpdate();
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir lista: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir lista no banco de dados.", e);
        } catch (Exception e) {
            System.err.println("Erro de serialização/deserialização: " + e.getMessage());
            throw new RuntimeException("Erro de processamento JSON.", e);
        }
    }

    public List<Lista> findAll() {
        List<Lista> listas = new ArrayList<>();
        String sql = "SELECT id, nomelista, campos FROM listas";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String nomeLista = rs.getString("nomelista");
                String jsonCampos = rs.getString("campos");

                // Converte String (JSON) do banco de volta para JsonNode
                JsonNode campos = objectMapper.readTree(jsonCampos != null ? jsonCampos : "{}");

                listas.add(new Lista(id, nomeLista, campos));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar listas: " + e.getMessage());
            throw new RuntimeException("Falha na comunicação com o banco de dados.", e);
        } catch (Exception e) {
            System.err.println("Erro de serialização/deserialização: " + e.getMessage());
            throw new RuntimeException("Erro de processamento JSON.", e);
        }
        return listas;
    }
}