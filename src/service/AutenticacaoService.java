package service;

import dao.UsuarioDAO;
import model.Usuario;
import util.CpfUtil;
import util.ValidacaoException;

public class AutenticacaoService {
    private final UsuarioDAO usuarioDAO;

    public AutenticacaoService() {
        this(new UsuarioDAO());
    }

    public AutenticacaoService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String cpf, String senha) {
        Usuario usuario = usuarioDAO.buscarPorCpf(CpfUtil.formatar(cpf))
                .orElseThrow(() -> new ValidacaoException("CPF ou senha inválidos."));
        if (!usuario.autenticar(senha)) {
            throw new ValidacaoException("CPF ou senha inválidos.");
        }
        return usuario;
    }
}
