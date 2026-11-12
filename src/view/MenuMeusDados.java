package view;

import java.util.List;

import model.Produtor;
import service.ProdutorService;

public class MenuMeusDados extends MenuProdutorBase {
    private final ProdutorService produtorService = new ProdutorService();
    private boolean contaExcluida;

    public MenuMeusDados(Console console, Produtor produtor) {
        super(console, produtor);
    }

    public boolean isContaExcluida() {
        return contaExcluida;
    }

    @Override
    protected String getTitulo() {
        return "Meus dados";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Ver meus dados", "Editar meus dados", "Alterar senha", "Excluir minha conta");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> {
                console.mensagem("  " + produtor);
                console.mensagem(String.format("  Terrenos: %d | Área total: %.2f ha",
                        terrenoService.listar(produtor).size(),
                        terrenoService.listar(produtor).stream().mapToDouble(t -> t.getTamanhoHectares()).sum()));
            }
            case 2 -> {
                Produtor copia = produtorService.buscarPorId(produtor.getId());
                FormularioProdutor.editar(console, copia);
                produtorService.atualizar(copia);
                produtor.setNome(copia.getNome());
                produtor.setCpf(copia.getCpf());
                produtor.setTelefone(copia.getTelefone());
                produtor.setEmail(copia.getEmail());
                produtor.setEndereco(copia.getEndereco());
                console.sucesso("Dados atualizados.");
            }
            case 3 -> {
                String atual = console.lerTexto("Senha atual");
                String nova = console.lerTexto("Nova senha (mínimo " + Produtor.TAMANHO_MINIMO_SENHA + " caracteres)");
                produtorService.alterarSenha(produtor, atual, nova);
                console.sucesso("Senha alterada.");
            }
            case 4 -> {
                console.alerta("Todos os seus terrenos, plantios, colheitas, vendas e avisos serão apagados.");
                if (console.confirmar("Tem certeza que deseja excluir sua conta")) {
                    produtorService.excluir(produtor.getId());
                    contaExcluida = true;
                    console.sucesso("Conta excluída. Até logo!");
                    return false;
                }
            }
            default -> { }
        }
        return true;
    }
}
