package model;

import java.time.LocalDate;

public class RegistroPraga extends RegistroMonitoramento {
    private String tipoPraga;
    private NivelRisco nivelInfestacao;

    public RegistroPraga() {
        super();
    }

    public RegistroPraga(int idPlantio, LocalDate dataRegistro, String observacao,
                         String tipoPraga, NivelRisco nivelInfestacao) {
        super(idPlantio, dataRegistro, observacao);
        this.tipoPraga = tipoPraga;
        this.nivelInfestacao = nivelInfestacao;
    }

    @Override
    public String getTipoMonitoramento() {
        return TIPO_PRAGA;
    }

    @Override
    public String gerarAlerta() {
        if (nivelInfestacao == NivelRisco.ALTO) {
            return "Infestação ALTA de " + tipoPraga + " no plantio #" + getIdPlantio()
                    + ": aplicar controle de pragas imediatamente.";
        }
        if (nivelInfestacao == NivelRisco.MEDIO) {
            return "Infestação média de " + tipoPraga + " no plantio #" + getIdPlantio()
                    + ": reforçar o monitoramento nos próximos dias.";
        }
        return null;
    }

    @Override
    public String getDetalhes() {
        return "Praga: " + tipoPraga + " (infestação " + nivelInfestacao + ")";
    }

    public String getTipoPraga() {
        return tipoPraga;
    }

    public void setTipoPraga(String tipoPraga) {
        this.tipoPraga = tipoPraga;
    }

    public NivelRisco getNivelInfestacao() {
        return nivelInfestacao;
    }

    public void setNivelInfestacao(NivelRisco nivelInfestacao) {
        this.nivelInfestacao = nivelInfestacao;
    }
}
