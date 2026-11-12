package view;

import java.util.List;

import model.Administrador;
import model.Produtor;
import model.Usuario;
import service.AutenticacaoService;
import service.ProdutorService;

public class MenuInicial extends Menu {
    private final AutenticacaoService autenticacaoService = new AutenticacaoService();
    private final ProdutorService produtorService = new ProdutorService();

    public MenuInicial(Console console) {
        super(console);
    }

    @Override
    protected String getTitulo() {
        return "AgroConecta - Gestão para micro produtores rurais";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Entrar", "Sou produtor e quero me cadastrar");
    }

    @Override
    protected String getTextoSair() {
        return "Sair do sistema";
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> entrar();
            case 2 -> cadastrarProdutor();
            default -> { }
        }
        return true;
    }

    private void entrar() {
        String cpf = console.lerTexto("CPF");
        String senha = console.lerTexto("Senha");
        Usuario usuario = autenticacaoService.autenticar(cpf, senha);
        console.sucesso(usuario.gerarAlerta());

        Menu menu;
        if (usuario instanceof Administrador administrador) {
            menu = new MenuAdministrador(console, administrador);
        } else {
            menu = new MenuProdutor(console, (Produtor) usuario);
        }
        menu.exibir();
    }

    private void cadastrarProdutor() {
        console.titulo("Cadastro de produtor");
        Produtor produtor = FormularioProdutor.ler(console);
        String senha = console.lerTexto("Crie uma senha (mínimo " + Produtor.TAMANHO_MINIMO_SENHA + " caracteres)");
        produtorService.cadastrar(produtor, senha);
        console.sucesso("Cadastro realizado! Seu código é #" + produtor.getId() + ". Agora é só entrar com seu CPF.");
    }
}
