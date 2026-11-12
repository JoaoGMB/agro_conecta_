package view;

import java.util.List;

import model.Plantio;
import model.Produtor;
import model.StatusPlantio;

public class MenuPlantios extends MenuProdutorBase {
    public MenuPlantios(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Plantios";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Listar meus plantios", "Iniciar plantio", "Editar plantio", "Finalizar plantio",
                "Excluir plantio");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> console.listar("Nenhum plantio registrado.", plantioService.listar(produtor));
            case 2 -> iniciar();
            case 3 -> editar();
            case 4 -> finalizar();
            case 5 -> excluir();
            default -> { }
        }
        return true;
    }

    private void iniciar() {
        Plantio plantio = new Plantio();
        plantio.setIdTerreno(escolherTerreno());
        plantio.setDataInicio(console.lerData("Data de início"));
        plantio.setFotosPlantacao(console.lerTextoOpcional("Fotos da plantação (caminhos ou links separados por ;)"));
        plantioService.iniciarPlantio(produtor, plantio);
        console.sucesso("Plantio #" + plantio.getId() + " iniciado.");
    }

    private void editar() {
        Plantio plantio = plantioService.buscarDoProdutor(produtor, escolherPlantio());
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        plantio.setDataInicio(console.lerDataEdicao("Data de início", plantio.getDataInicio()));
        plantio.setFotosPlantacao(console.lerTextoEdicao("Fotos da plantação", plantio.getFotosPlantacao()));
        if (console.confirmar("Alterar o status (atual: " + plantio.getStatus().getDescricao() + ")")) {
            StatusPlantio novoStatus = console.lerEnum("Novo status", StatusPlantio.values());
            plantio.atualizarStatus(novoStatus, novoStatus == StatusPlantio.EM_ANDAMENTO
                    ? null
                    : console.lerDataEdicao("Data de término", plantio.getDataTermino()));
        }
        plantioService.atualizar(produtor, plantio);
        console.sucesso("Plantio atualizado.");
    }

    private void finalizar() {
        int id = escolherPlantio();
        plantioService.finalizarPlantio(produtor, id, console.lerData("Data de término"));
        console.sucesso("Plantio #" + id + " finalizado.");
    }

    private void excluir() {
        int id = escolherPlantio();
        plantioService.buscarDoProdutor(produtor, id);
        console.alerta("Mudas, monitoramentos, colheitas e vendas deste plantio também serão apagados.");
        if (console.confirmar("Confirma a exclusão do plantio #" + id)) {
            plantioService.excluir(produtor, id);
            console.sucesso("Plantio excluído.");
        }
    }
}
