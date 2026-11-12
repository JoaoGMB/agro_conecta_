package model;

public enum StatusPlantio {
    EM_ANDAMENTO("Em andamento"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPlantio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
