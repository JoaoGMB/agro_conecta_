package service;

import java.util.List;

import dao.UsuarioDAO;
import model.Administrador;
import model.Produtor;
import model.TipoUsuario;
import model.Usuario;
import util.CpfUtil;
import util.ValidacaoException;
import util.Validador;

public class ProdutorService {
    private final UsuarioDAO usuarioDAO;

    public ProdutorService() {
        this(new UsuarioDAO());
    }

    public ProdutorService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Produtor cadastrar(Produtor produtor, String senha) {
        validarDados(produtor, 0);
        if (senha == null || senha.length() < Produtor.TAMANHO_MINIMO_SENHA) {
            throw new ValidacaoException("A senha deve ter pelo menos " + Produtor.TAMANHO_MINIMO_SENHA + " caracteres.");
        }
        produtor.definirSenha(senha);
        return (Produtor) usuarioDAO.inserir(produtor);
    }

    public List<Produtor> listar() {
        return usuarioDAO.listarPorTipo(TipoUsuario.PRODUTOR).stream()
                .map(Produtor.class::cast)
                .toList();
    }

    public Produtor buscarPorId(int id) {
        Usuario usuario = usuarioDAO.buscarPorId(id)
                .orElseThrow(() -> new ValidacaoException("Produtor #" + id + " não encontrado."));
        if (!(usuario instanceof Produtor produtor)) {
            throw new ValidacaoException("O usuário #" + id + " não é um produtor.");
        }
        return produtor;
    }

    public List<Produtor> buscarPorNome(String trecho) {
        return usuarioDAO.buscarPorNome(trecho == null ? "" : trecho.trim(), TipoUsuario.PRODUTOR).stream()
                .map(Produtor.class::cast)
                .toList();
    }

    public void atualizar(Produtor produtor) {
        validarDados(produtor, produtor.getId());
        if (!usuarioDAO.atualizar(produtor)) {
            throw new ValidacaoException("Produtor #" + produtor.getId() + " não encontrado.");
        }
    }

    public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha) {
        if (!usuario.autenticar(senhaAtual)) {
            throw new ValidacaoException("A senha atual não confere.");
        }
        int minimo = usuario instanceof Administrador
                ? Administrador.TAMANHO_MINIMO_SENHA
                : Produtor.TAMANHO_MINIMO_SENHA;
        if (novaSenha == null || novaSenha.length() < minimo) {
            throw new ValidacaoException("A nova senha deve ter pelo menos " + minimo + " caracteres.");
        }
        usuario.definirSenha(novaSenha);
        usuarioDAO.atualizar(usuario);
    }

    public void excluir(int id) {
        buscarPorId(id);
        usuarioDAO.excluir(id);
    }

    private void validarDados(Produtor produtor, int idAtual) {
        Validador.exigirTexto(produtor.getNome(), "Nome completo");
        Validador.exigirTamanhoMaximo(produtor.getNome(), 150, "Nome completo");
        produtor.setNome(produtor.getNome().trim());

        if (!CpfUtil.isValido(produtor.getCpf())) {
            throw new ValidacaoException("CPF inválido: " + produtor.getCpf());
        }
        produtor.setCpf(CpfUtil.formatar(produtor.getCpf()));
        if (usuarioDAO.existeCpf(produtor.getCpf(), idAtual)) {
            throw new ValidacaoException("Já existe um usuário cadastrado com o CPF " + produtor.getCpf() + ".");
        }

        Validador.exigirTamanhoMaximo(produtor.getTelefone(), 20, "Telefone");
        Validador.exigirEmailValido(produtor.getEmail());
        Validador.exigirTamanhoMaximo(produtor.getEmail(), 150, "E-mail");
        Validador.exigirTamanhoMaximo(produtor.getEndereco(), 255, "Endereço");
    }
}
