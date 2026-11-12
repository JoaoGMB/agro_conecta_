package model;

import java.time.LocalDate;

import util.DataUtil;

public abstract class RegistroMonitoramento {
    public static final String TIPO_PRAGA = "PRAGA";
    public static final String TIPO_DOENCA = "DOENCA";
    public static final String TIPO_CLIMA = "CLIMA";

    private int id;
    private int idPlantio;
    private LocalDate dataRegistro;
    private String observacao;

    protected RegistroMonitoramento() {
    }

    protected RegistroMonitoramento(int idPlantio, LocalDate dataRegistro, String observacao) {
        this.idPlantio = idPlantio;
        this.dataRegistro = dataRegistro;
        this.observacao = observacao;
    }

    public abstract String getTipoMonitoramento();

    public abstract String gerarAlerta();

    public abstract String getDetalhes();

    public boolean possuiAlerta() {
        return gerarAlerta() != null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlantio() {
        return idPlantio;
    }

    public void setIdPlantio(int idPlantio) {
        this.idPlantio = idPlantio;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | Plantio #%d | %s | %s | Obs: %s",
                id, DataUtil.formatar(dataRegistro), idPlantio, getTipoMonitoramento(), getDetalhes(),
                observacao == null || observacao.isBlank() ? "-" : observacao);
    }
}
