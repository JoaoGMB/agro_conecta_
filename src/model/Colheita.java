package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.DataUtil;

public class Colheita {
    private int id;
    private int idPlantio;
    private LocalDate dataColheita;
    private double quantidadeColhida;
    private double tempoSecagem;
    private String qualidadeProduto;
    private final List<Venda> vendas = new ArrayList<>();

    public Colheita() {
    }

    public Colheita(int idPlantio, LocalDate dataColheita, double quantidadeColhida,
                    double tempoSecagem, String qualidadeProduto) {
        this.idPlantio = idPlantio;
        this.dataColheita = dataColheita;
        this.quantidadeColhida = quantidadeColhida;
        this.tempoSecagem = tempoSecagem;
        this.qualidadeProduto = qualidadeProduto;
    }

    public void atualizarQualidade(String qualidadeProduto) {
        this.qualidadeProduto = qualidadeProduto;
    }

    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
    }

    public List<Venda> getVendas() {
        return Collections.unmodifiableList(vendas);
    }

    public double getTotalVendido() {
        return vendas.stream().mapToDouble(Venda::getQuantidadeVendida).sum();
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

    public LocalDate getDataColheita() {
        return dataColheita;
    }

    public void setDataColheita(LocalDate dataColheita) {
        this.dataColheita = dataColheita;
    }

    public double getQuantidadeColhida() {
        return quantidadeColhida;
    }

    public void setQuantidadeColhida(double quantidadeColhida) {
        this.quantidadeColhida = quantidadeColhida;
    }

    public double getTempoSecagem() {
        return tempoSecagem;
    }

    public void setTempoSecagem(double tempoSecagem) {
        this.tempoSecagem = tempoSecagem;
    }

    public String getQualidadeProduto() {
        return qualidadeProduto;
    }

    public void setQualidadeProduto(String qualidadeProduto) {
        this.qualidadeProduto = qualidadeProduto;
    }

    @Override
    public String toString() {
        return String.format("#%d | Plantio #%d | %s | %.2f kg | Secagem %.1f dias | Qualidade: %s",
                id, idPlantio, DataUtil.formatar(dataColheita), quantidadeColhida, tempoSecagem,
                qualidadeProduto);
    }
}
