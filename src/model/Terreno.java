package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Terreno {
    private int id;
    private int idProdutor;
    private String localizacao;
    private double tamanhoHectares;
    private String qualidadeSolo;
    private final List<Plantio> plantios = new ArrayList<>();

    public Terreno() {
    }

    public Terreno(int idProdutor, String localizacao, double tamanhoHectares, String qualidadeSolo) {
        this.idProdutor = idProdutor;
        this.localizacao = localizacao;
        this.tamanhoHectares = tamanhoHectares;
        this.qualidadeSolo = qualidadeSolo;
    }

    public boolean precisaAtualizarDados() {
        return localizacao == null || localizacao.isBlank()
                || qualidadeSolo == null || qualidadeSolo.isBlank()
                || tamanhoHectares <= 0;
    }

    public void atualizarDados(String localizacao, double tamanhoHectares, String qualidadeSolo) {
        this.localizacao = localizacao;
        this.tamanhoHectares = tamanhoHectares;
        this.qualidadeSolo = qualidadeSolo;
    }

    public void adicionarPlantio(Plantio plantio) {
        plantios.add(plantio);
    }

    public List<Plantio> getPlantios() {
        return Collections.unmodifiableList(plantios);
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

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public double getTamanhoHectares() {
        return tamanhoHectares;
    }

    public void setTamanhoHectares(double tamanhoHectares) {
        this.tamanhoHectares = tamanhoHectares;
    }

    public String getQualidadeSolo() {
        return qualidadeSolo;
    }

    public void setQualidadeSolo(String qualidadeSolo) {
        this.qualidadeSolo = qualidadeSolo;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | %.2f ha | Solo: %s",
                id, localizacao, tamanhoHectares,
                qualidadeSolo == null || qualidadeSolo.isBlank() ? "não informado" : qualidadeSolo);
    }
}
