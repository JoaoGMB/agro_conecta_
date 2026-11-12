package model;

public class Muda {
    private int id;
    private int idPlantio;
    private int quantidade;
    private String genetica;
    private double valorAquisicao;

    public Muda() {
    }

    public Muda(int idPlantio, int quantidade, String genetica, double valorAquisicao) {
        this.idPlantio = idPlantio;
        this.quantidade = quantidade;
        this.genetica = genetica;
        this.valorAquisicao = valorAquisicao;
    }

    public double calcularCustoTotal() {
        return quantidade * valorAquisicao;
    }

    public void atualizarQuantidade(int quantidade) {
        this.quantidade = quantidade;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getGenetica() {
        return genetica;
    }

    public void setGenetica(String genetica) {
        this.genetica = genetica;
    }

    public double getValorAquisicao() {
        return valorAquisicao;
    }

    public void setValorAquisicao(double valorAquisicao) {
        this.valorAquisicao = valorAquisicao;
    }

    @Override
    public String toString() {
        return String.format("#%d | Plantio #%d | %d mudas | Genética: %s | R$ %.2f/un | Custo total R$ %.2f",
                id, idPlantio, quantidade, genetica, valorAquisicao, calcularCustoTotal());
    }
}
