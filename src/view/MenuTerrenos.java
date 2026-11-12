package view;

import java.util.List;

import model.Produtor;
import model.Terreno;

public class MenuTerrenos extends MenuProdutorBase {
    public MenuTerrenos(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Terrenos";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Listar meus terrenos", "Cadastrar terreno", "Editar terreno", "Excluir terreno");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> console.listar("Nenhum terreno cadastrado.", terrenoService.listar(produtor));
            case 2 -> cadastrar();
            case 3 -> editar();
            case 4 -> excluir();
            default -> { }
        }
        return true;
    }

    private void cadastrar() {
        Terreno terreno = new Terreno();
        terreno.setLocalizacao(console.lerTexto("Localização (ex.: Sítio Boa Vista, Estrada X km 5)"));
        terreno.setTamanhoHectares(console.lerDecimal("Tamanho em hectares"));
        terreno.setQualidadeSolo(console.lerTextoOpcional("Qualidade do solo"));
        terrenoService.cadastrarTerreno(produtor, terreno);
        console.sucesso("Terreno #" + terreno.getId() + " cadastrado.");
    }

    private void editar() {
        Terreno terreno = terrenoService.buscarDoProdutor(produtor, escolherTerreno());
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        terreno.atualizarDados(
                console.lerTextoEdicao("Localização", terreno.getLocalizacao()),
                console.lerDecimalEdicao("Tamanho em hectares", terreno.getTamanhoHectares()),
                console.lerTextoEdicao("Qualidade do solo", terreno.getQualidadeSolo()));
        terrenoService.atualizarDados(produtor, terreno);
        console.sucesso("Terreno atualizado.");
    }

    private void excluir() {
        int id = escolherTerreno();
        terrenoService.buscarDoProdutor(produtor, id);
        console.alerta("Os plantios, colheitas e vendas deste terreno também serão apagados.");
        if (console.confirmar("Confirma a exclusão do terreno #" + id)) {
            terrenoService.excluir(produtor, id);
            console.sucesso("Terreno excluído.");
        }
    }
}
