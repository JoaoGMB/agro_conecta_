package view;

import java.util.List;

import model.Colheita;
import model.Muda;
import model.Plantio;
import model.Produtor;
import model.RegistroMonitoramento;
import model.Terreno;
import model.Venda;
import service.AvisoService;

public class MenuProdutor extends MenuProdutorBase {
    private final AvisoService avisoService = new AvisoService();

    public MenuProdutor(Console console, Produtor produtor) {
        super(console, produtor);
        int novos = avisoService.gerarAvisosAutomaticos(produtor);
        if (novos > 0) {
            console.alerta("Você tem " + novos + " aviso(s) novo(s). Veja em \"Avisos\".");
        }
    }

    @Override
    protected String getTitulo() {
        return "Produtor: " + produtor.getNome();
    }

    @Override
    protected List<String> getOpcoes() {
        int pendentes = avisoService.listarPendentes(produtor).size();
        return List.of(
                "Avisos (" + pendentes + " pendente" + (pendentes == 1 ? "" : "s") + ")",
                "Terrenos",
                "Plantios",
                "Mudas",
                "Monitoramento (pragas, doenças e chuva)",
                "Colheitas",
                "Vendas",
                "Histórico completo de um terreno",
                "Meus dados");
    }

    @Override
    protected String getTextoSair() {
        return "Sair (logout)";
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> new MenuAvisos(console, produtor).exibir();
            case 2 -> new MenuTerrenos(console, produtor).exibir();
            case 3 -> new MenuPlantios(console, produtor).exibir();
            case 4 -> new MenuMudas(console, produtor).exibir();
            case 5 -> new MenuMonitoramento(console, produtor).exibir();
            case 6 -> new MenuColheitas(console, produtor).exibir();
            case 7 -> new MenuVendas(console, produtor).exibir();
            case 8 -> mostrarHistorico();
            case 9 -> {
                MenuMeusDados meusDados = new MenuMeusDados(console, produtor);
                meusDados.exibir();
                return !meusDados.isContaExcluida();
            }
            default -> { }
        }
        return true;
    }

    private void mostrarHistorico() {
        Terreno terreno = terrenoService.consultarHistorico(produtor, escolherTerreno());
        console.titulo("Histórico do terreno #" + terreno.getId());
        console.mensagem(terreno.toString());
        if (terreno.getPlantios().isEmpty()) {
            console.mensagem("  Nenhum plantio neste terreno.");
        }
        double faturamento = 0;
        for (Plantio plantio : terreno.getPlantios()) {
            console.mensagem("");
            console.mensagem("Plantio " + plantio);
            for (Muda muda : plantio.getMudas()) {
                console.mensagem("   Muda " + muda);
            }
            for (RegistroMonitoramento registro : plantio.getMonitoramentos()) {
                String alerta = registro.gerarAlerta();
                console.mensagem("   Monitoramento " + registro + (alerta != null ? "  <- ALERTA" : ""));
            }
            for (Colheita colheita : plantio.getColheitas()) {
                console.mensagem("   Colheita " + colheita);
                for (Venda venda : colheita.getVendas()) {
                    console.mensagem("      Venda " + venda);
                    faturamento += venda.calcularTotal();
                }
            }
        }
        console.mensagem("");
        console.mensagem(String.format("Faturamento total do terreno: R$ %.2f", faturamento));
    }
}
