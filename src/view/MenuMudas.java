package view;

import java.util.List;

import model.Muda;
import model.Produtor;
import service.MudaService;

public class MenuMudas extends MenuProdutorBase {
    private final MudaService mudaService = new MudaService();

    public MenuMudas(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Mudas";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Listar mudas de um plantio", "Registrar mudas", "Editar registro de mudas",
                "Excluir registro de mudas");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> listar();
            case 2 -> cadastrar();
            case 3 -> editar();
            case 4 -> excluir();
            default -> { }
        }
        return true;
    }

    private int listar() {
        int idPlantio = escolherPlantio();
        List<Muda> mudas = mudaService.listarPorPlantio(produtor, idPlantio);
        console.listar("Nenhuma muda registrada neste plantio.", mudas);
        if (!mudas.isEmpty()) {
            console.mensagem(String.format("  Custo total com mudas: R$ %.2f",
                    mudas.stream().mapToDouble(Muda::calcularCustoTotal).sum()));
        }
        return idPlantio;
    }

    private void cadastrar() {
        Muda muda = new Muda();
        muda.setIdPlantio(escolherPlantio());
        muda.setQuantidade(console.lerInteiro("Quantidade de mudas"));
        muda.setGenetica(console.lerTexto("Genética / variedade"));
        muda.setValorAquisicao(console.lerDecimal("Valor pago por muda (R$)"));
        mudaService.cadastrarMuda(produtor, muda);
        console.sucesso("Mudas registradas (#" + muda.getId() + ").");
    }

    private void editar() {
        listar();
        Muda muda = mudaService.buscarDoProdutor(produtor, console.lerInteiro("Código do registro de muda"));
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        muda.atualizarQuantidade(console.lerInteiroEdicao("Quantidade", muda.getQuantidade()));
        muda.setGenetica(console.lerTextoEdicao("Genética", muda.getGenetica()));
        muda.setValorAquisicao(console.lerDecimalEdicao("Valor por muda", muda.getValorAquisicao()));
        mudaService.atualizar(produtor, muda);
        console.sucesso("Registro de mudas atualizado.");
    }

    private void excluir() {
        listar();
        int id = console.lerInteiro("Código do registro de muda");
        mudaService.buscarDoProdutor(produtor, id);
        if (console.confirmar("Confirma a exclusão do registro #" + id)) {
            mudaService.excluir(produtor, id);
            console.sucesso("Registro excluído.");
        }
    }
}
