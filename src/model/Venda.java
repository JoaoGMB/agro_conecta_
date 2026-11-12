package model;

import java.time.LocalDate;

import util.DataUtil;

public class Venda {
    private int id;
    private int idColheita;
    private double quantidadeDisponivel;
    private double quantidadeVendida;
    private double valor;
    private LocalDate dataVenda;

    public Venda() {
    }

    public Venda(int idColheita, double quantidadeVendida, double valor, LocalDate dataVenda) {
        this.idColheita = idColheita;
        this.quantidadeVendida = quantidadeVendida;
        this.valor = valor;
        this.dataVenda = dataVenda;
    }

    public double calcularTotal() {
        return quantidadeVendida * valor;
    }

    public double getSaldoAposVenda() {
        return quantidadeDisponivel - quantidadeVendida;
    }

    public void atualizarQuantidadeDisponivel(double quantidadeColhida, double totalJaVendido) {
        this.quantidadeDisponivel = quantidadeColhida - totalJaVendido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdColheita() {
        return idColheita;
    }

    public void setIdColheita(int idColheita) {
        this.idColheita = idColheita;
    }

    public double getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(double quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public double getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(double quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public String toString() {
        return String.format("#%d | Colheita #%d | %s | Vendido %.2f kg de %.2f kg | R$ %.2f/kg | Total R$ %.2f",
                id, idColheita, DataUtil.formatar(dataVenda), quantidadeVendida, quantidadeDisponivel,
                valor, calcularTotal());
    }
}
