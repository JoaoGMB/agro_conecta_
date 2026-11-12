package view;

import java.util.List;

import model.Produtor;
import model.Venda;
import service.VendaService;

public class MenuVendas extends MenuProdutorBase {
    private final VendaService vendaService = new VendaService();

    public MenuVendas(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Vendas";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Listar vendas de uma colheita", "Registrar venda", "Editar venda", "Excluir venda");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> listar(escolherColheita());
            case 2 -> registrar();
            case 3 -> editar();
            case 4 -> excluir();
            default -> { }
        }
        return true;
    }

    private void listar(int idColheita) {
        List<Venda> vendas = vendaService.listarPorColheita(produtor, idColheita);
        console.listar("Nenhuma venda registrada para esta colheita.", vendas);
        if (!vendas.isEmpty()) {
            console.mensagem(String.format("  Total faturado: R$ %.2f",
                    vendas.stream().mapToDouble(Venda::calcularTotal).sum()));
        }
    }

    private void registrar() {
        Venda venda = new Venda();
        venda.setIdColheita(escolherColheita());
        venda.setDataVenda(console.lerData("Data da venda"));
        venda.setQuantidadeVendida(console.lerDecimal("Quantidade vendida (kg)"));
        venda.setValor(console.lerDecimal("Preço por kg (R$)"));
        vendaService.registrarVenda(produtor, venda);
        console.sucesso(String.format("Venda #%d registrada. Total R$ %.2f. Saldo da colheita: %.2f kg.",
                venda.getId(), venda.calcularTotal(), venda.getSaldoAposVenda()));
    }

    private void editar() {
        int idColheita = escolherColheita();
        listar(idColheita);
        Venda venda = vendaService.buscarDoProdutor(produtor, console.lerInteiro("Código da venda"));
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        venda.setDataVenda(console.lerDataEdicao("Data da venda", venda.getDataVenda()));
        venda.setQuantidadeVendida(console.lerDecimalEdicao("Quantidade vendida (kg)", venda.getQuantidadeVendida()));
        venda.setValor(console.lerDecimalEdicao("Preço por kg (R$)", venda.getValor()));
        vendaService.atualizar(produtor, venda);
        console.sucesso(String.format("Venda atualizada. Total R$ %.2f.", venda.calcularTotal()));
    }

    private void excluir() {
        int idColheita = escolherColheita();
        listar(idColheita);
        int id = console.lerInteiro("Código da venda");
        vendaService.buscarDoProdutor(produtor, id);
        if (console.confirmar("Confirma a exclusão da venda #" + id)) {
            vendaService.excluir(produtor, id);
            console.sucesso("Venda excluída.");
        }
    }
}
