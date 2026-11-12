package view;

import java.util.List;

import model.Administrador;
import model.Produtor;
import service.ProdutorService;

public class MenuAdministrador extends Menu {
    private final Administrador administrador;
    private final ProdutorService produtorService = new ProdutorService();

    public MenuAdministrador(Console console, Administrador administrador) {
        super(console);
        this.administrador = administrador;
    }

    @Override
    protected String getTitulo() {
        return "Administrador: " + administrador.getNome() + " | Gestão de produtores";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of(
                "Listar produtores",
                "Buscar produtor por nome",
                "Ver produtor por código",
                "Cadastrar produtor",
                "Editar produtor",
                "Excluir produtor",
                "Alterar minha senha");
    }

    @Override
    protected String getTextoSair() {
        return "Sair (logout)";
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> console.listar("Nenhum produtor cadastrado.", produtorService.listar());
            case 2 -> console.listar("Nenhum produtor encontrado.",
                    produtorService.buscarPorNome(console.lerTexto("Parte do nome")));
            case 3 -> console.mensagem("  " + produtorService.buscarPorId(console.lerInteiro("Código do produtor")));
            case 4 -> cadastrar();
            case 5 -> editar();
            case 6 -> excluir();
            case 7 -> alterarSenha();
            default -> { }
        }
        return true;
    }

    private void cadastrar() {
        Produtor produtor = FormularioProdutor.ler(console);
        String senha = console.lerTexto("Senha inicial (mínimo " + Produtor.TAMANHO_MINIMO_SENHA + " caracteres)");
        produtorService.cadastrar(produtor, senha);
        console.sucesso("Produtor #" + produtor.getId() + " cadastrado.");
    }

    private void editar() {
        Produtor produtor = produtorService.buscarPorId(console.lerInteiro("Código do produtor"));
        FormularioProdutor.editar(console, produtor);
        produtorService.atualizar(produtor);
        console.sucesso("Produtor #" + produtor.getId() + " atualizado.");
    }

    private void excluir() {
        Produtor produtor = produtorService.buscarPorId(console.lerInteiro("Código do produtor"));
        console.mensagem("  " + produtor);
        console.alerta("Todos os terrenos, plantios, colheitas, vendas e avisos deste produtor também serão apagados.");
        if (console.confirmar("Confirma a exclusão")) {
            produtorService.excluir(produtor.getId());
            console.sucesso("Produtor excluído.");
        }
    }

    private void alterarSenha() {
        String atual = console.lerTexto("Senha atual");
        String nova = console.lerTexto("Nova senha (mínimo " + Administrador.TAMANHO_MINIMO_SENHA + " caracteres)");
        produtorService.alterarSenha(administrador, atual, nova);
        console.sucesso("Senha alterada.");
    }
}
