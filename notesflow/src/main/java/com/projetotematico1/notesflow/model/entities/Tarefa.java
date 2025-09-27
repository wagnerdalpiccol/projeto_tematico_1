package com.projetotematico1.notesflow.model.entities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Tarefa {
    private UUID id;
    private String descricao;
    private Integer status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer prioridade;

    public Tarefa() {
    }

    public Tarefa(String descricao, Integer status, LocalDate dataInicio, LocalDate dataFim, Integer prioridade) {
        this.descricao = descricao;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.prioridade = prioridade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return Objects.equals(id, tarefa.id) && Objects.equals(descricao, tarefa.descricao) && Objects.equals(status, tarefa.status) && Objects.equals(dataInicio, tarefa.dataInicio) && Objects.equals(dataFim, tarefa.dataFim) && Objects.equals(prioridade, tarefa.prioridade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, status, dataInicio, dataFim, prioridade);
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", prioridade=" + prioridade +
                '}';
    }
}
