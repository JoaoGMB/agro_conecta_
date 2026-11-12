package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.DataUtil;

public class Plantio {
    private int id;
    private int idTerreno;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private StatusPlantio status = StatusPlantio.EM_ANDAMENTO;
    private String fotosPlantacao;

    private final List<Muda> mudas = new ArrayList<>();
    private final List<RegistroMonitoramento> monitoramentos = new ArrayList<>();
    private final List<Colheita> colheitas = new ArrayList<>();

    public Plantio() {
    }

    public Plantio(int idTerreno, LocalDate dataInicio, String fotosPlantacao) {
        this.idTerreno = idTerreno;
        this.dataInicio = dataInicio;
        this.fotosPlantacao = fotosPlantacao;
        this.status = StatusPlantio.EM_ANDAMENTO;
    }

    public void iniciarPlantio() {
        this.status = StatusPlantio.EM_ANDAMENTO;
        this.dataTermino = null;
    }

    public void atualizarStatus(StatusPlantio novoStatus, LocalDate dataTermino) {
        this.status = novoStatus;
        this.dataTermino = novoStatus == StatusPlantio.EM_ANDAMENTO ? null : dataTermino;
    }

    public void finalizarPlantio(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
        this.status = StatusPlantio.FINALIZADO;
    }

    public boolean isEmAndamento() {
        return status == StatusPlantio.EM_ANDAMENTO;
    }

    public void adicionarMuda(Muda muda) {
        mudas.add(muda);
    }

    public void adicionarMonitoramento(RegistroMonitoramento registro) {
        monitoramentos.add(registro);
    }

    public void adicionarColheita(Colheita colheita) {
        colheitas.add(colheita);
    }

    public List<Muda> getMudas() {
        return Collections.unmodifiableList(mudas);
    }

    public List<RegistroMonitoramento> getMonitoramentos() {
        return Collections.unmodifiableList(monitoramentos);
    }

    public List<Colheita> getColheitas() {
        return Collections.unmodifiableList(colheitas);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTerreno() {
        return idTerreno;
    }

    public void setIdTerreno(int idTerreno) {
        this.idTerreno = idTerreno;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public StatusPlantio getStatus() {
        return status;
    }

    public void setStatus(StatusPlantio status) {
        this.status = status;
    }

    public String getFotosPlantacao() {
        return fotosPlantacao;
    }

    public void setFotosPlantacao(String fotosPlantacao) {
        this.fotosPlantacao = fotosPlantacao;
    }

    @Override
    public String toString() {
        return String.format("#%d | Terreno #%d | Início %s | Término %s | %s | Fotos: %s",
                id, idTerreno, DataUtil.formatar(dataInicio), DataUtil.formatar(dataTermino),
                status.getDescricao(),
                fotosPlantacao == null || fotosPlantacao.isBlank() ? "-" : fotosPlantacao);
    }
}
