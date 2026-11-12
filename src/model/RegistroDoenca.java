package model;

import java.time.LocalDate;

public class RegistroDoenca extends RegistroMonitoramento {
    private String tipoDoenca;
    private NivelRisco nivelGravidade;

    public RegistroDoenca() {
        super();
    }

    public RegistroDoenca(int idPlantio, LocalDate dataRegistro, String observacao,
                          String tipoDoenca, NivelRisco nivelGravidade) {
        super(idPlantio, dataRegistro, observacao);
        this.tipoDoenca = tipoDoenca;
        this.nivelGravidade = nivelGravidade;
    }

    @Override
    public String getTipoMonitoramento() {
        return TIPO_DOENCA;
    }

    @Override
    public String gerarAlerta() {
        if (nivelGravidade == NivelRisco.ALTO) {
            return "Doença grave (" + tipoDoenca + ") no plantio #" + getIdPlantio()
                    + ": isolar a área e procurar orientação técnica.";
        }
        if (nivelGravidade == NivelRisco.MEDIO) {
            return "Doença moderada (" + tipoDoenca + ") no plantio #" + getIdPlantio()
                    + ": iniciar tratamento preventivo.";
        }
        return null;
    }

    @Override
    public String getDetalhes() {
        return "Doença: " + tipoDoenca + " (gravidade " + nivelGravidade + ")";
    }

    public String getTipoDoenca() {
        return tipoDoenca;
    }

    public void setTipoDoenca(String tipoDoenca) {
        this.tipoDoenca = tipoDoenca;
    }

    public NivelRisco getNivelGravidade() {
        return nivelGravidade;
    }

    public void setNivelGravidade(NivelRisco nivelGravidade) {
        this.nivelGravidade = nivelGravidade;
    }
}
