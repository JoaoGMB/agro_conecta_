package view;

import java.util.List;

import model.Colheita;
import model.Produtor;

public class MenuColheitas extends MenuProdutorBase {
    public MenuColheitas(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Colheitas";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Listar colheitas de um plantio", "Registrar colheita", "Editar colheita", "Excluir colheita");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> listar();
            case 2 -> registrar();
            case 3 -> editar();
            case 4 -> excluir();
            default -> { }
        }
        return true;
    }

    private void listar() {
        int idPlantio = escolherPlantio();
        List<Colheita> colheitas = colheitaService.listarPorPlantio(produtor, idPlantio);
        if (colheitas.isEmpty()) {
            console.mensagem("Nenhuma colheita registrada neste plantio.");
        }
        for (Colheita colheita : colheitas) {
            console.mensagem(String.format("  %s | Saldo para venda: %.2f kg",
                    colheita, colheitaService.calcularSaldo(colheita)));
        }
    }

    private void registrar() {
        Colheita colheita = new Colheita();
        colheita.setIdPlantio(escolherPlantio());
        colheita.setDataColheita(console.lerData("Data da colheita"));
        colheita.setQuantidadeColhida(console.lerDecimal("Quantidade colhida (kg)"));
        colheita.setTempoSecagem(console.lerDecimal("Tempo de secagem (dias)"));
        colheita.setQualidadeProduto(console.lerTexto("Qualidade do produto (ex.: tipo A, boa, regular)"));
        colheitaService.registrarColheita(produtor, colheita);
        console.sucesso("Colheita #" + colheita.getId() + " registrada.");
    }

    private void editar() {
        Colheita colheita = colheitaService.buscarDoProdutor(produtor, escolherColheita());
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        colheita.setDataColheita(console.lerDataEdicao("Data da colheita", colheita.getDataColheita()));
        colheita.setQuantidadeColhida(console.lerDecimalEdicao("Quantidade colhida (kg)", colheita.getQuantidadeColhida()));
        colheita.setTempoSecagem(console.lerDecimalEdicao("Tempo de secagem (dias)", colheita.getTempoSecagem()));
        colheita.atualizarQualidade(console.lerTextoEdicao("Qualidade do produto", colheita.getQualidadeProduto()));
        colheitaService.atualizar(produtor, colheita);
        console.sucesso("Colheita atualizada.");
    }

    private void excluir() {
        int id = escolherColheita();
        colheitaService.buscarDoProdutor(produtor, id);
        console.alerta("As vendas desta colheita também serão apagadas.");
        if (console.confirmar("Confirma a exclusão da colheita #" + id)) {
            colheitaService.excluir(produtor, id);
            console.sucesso("Colheita excluída.");
        }
    }
}
