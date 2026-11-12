package model;

import java.time.LocalDate;

public class RegistroClima extends RegistroMonitoramento {
    public static final double LIMITE_CHUVA_EXCESSIVA_MM = 100.0;
    public static final double LIMITE_CHUVA_BAIXA_MM = 5.0;

    private double volumeChuva;

    public RegistroClima() {
        super();
    }

    public RegistroClima(int idPlantio, LocalDate dataRegistro, String observacao, double volumeChuva) {
        super(idPlantio, dataRegistro, observacao);
        this.volumeChuva = volumeChuva;
    }

    @Override
    public String getTipoMonitoramento() {
        return TIPO_CLIMA;
    }

    @Override
    public String gerarAlerta() {
        if (volumeChuva >= LIMITE_CHUVA_EXCESSIVA_MM) {
            return String.format("Chuva excessiva (%.1f mm) no plantio #%d: verificar drenagem e erosão.",
                    volumeChuva, getIdPlantio());
        }
        if (volumeChuva < LIMITE_CHUVA_BAIXA_MM) {
            return String.format("Pouca chuva (%.1f mm) no plantio #%d: verificar a irrigação.",
                    volumeChuva, getIdPlantio());
        }
        return null;
    }

    @Override
    public String getDetalhes() {
        return String.format("Chuva: %.1f mm", volumeChuva);
    }

    public double getVolumeChuva() {
        return volumeChuva;
    }

    public void setVolumeChuva(double volumeChuva) {
        this.volumeChuva = volumeChuva;
    }
}
