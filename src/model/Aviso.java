package model;

import java.time.LocalDate;

import util.DataUtil;

public class Aviso {
    private int id;
    private int idProdutor;
    private String descricao;
    private LocalDate dataAviso;
    private StatusAviso statusConclusao = StatusAviso.PENDENTE;

    public Aviso() {
    }

    public Aviso(int idProdutor, String descricao, LocalDate dataAviso) {
        this.idProdutor = idProdutor;
        this.descricao = descricao;
        this.dataAviso = dataAviso;
        this.statusConclusao = StatusAviso.PENDENTE;
    }

    public void concluirAviso() {
        this.statusConclusao = StatusAviso.CONCLUIDO;
    }

    public boolean isPendente() {
        return statusConclusao == StatusAviso.PENDENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProdutor() {
        return idProdutor;
    }

    public void setIdProdutor(int idProdutor) {
        this.idProdutor = idProdutor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataAviso() {
        return dataAviso;
    }

    public void setDataAviso(LocalDate dataAviso) {
        this.dataAviso = dataAviso;
    }

    public StatusAviso getStatusConclusao() {
        return statusConclusao;
    }

    public void setStatusConclusao(StatusAviso statusConclusao) {
        this.statusConclusao = statusConclusao;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | [%s] %s",
                id, DataUtil.formatar(dataAviso), statusConclusao, descricao);
    }
}
