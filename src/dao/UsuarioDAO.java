package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Administrador;
import model.Produtor;
import model.TipoUsuario;
import model.Usuario;

public class UsuarioDAO extends AbstractDAO<Usuario> {
    @Override
    protected String getTabela() {
        return "usuario";
    }

    @Override
    protected String getColunaId() {
        return "id_usuario";
    }

    @Override
    protected Usuario mapear(ResultSet rs) throws SQLException {
        TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo_usuario"));
        Usuario usuario = tipo == TipoUsuario.ADMINISTRADOR ? new Administrador() : new Produtor();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenhaHash(rs.getString("senha"));
        usuario.setEndereco(rs.getString("endereco"));
        return usuario;
    }

    @Override
    public Usuario inserir(Usuario usuario) {
        int id = inserirRetornandoId(
                "INSERT INTO usuario (nome, cpf, telefone, email, senha, endereco, tipo_usuario) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                usuario.getNome(), usuario.getCpf(), nuloSeVazio(usuario.getTelefone()),
                nuloSeVazio(usuario.getEmail()), usuario.getSenhaHash(), nuloSeVazio(usuario.getEndereco()),
                usuario.getTipoUsuario());
        usuario.setId(id);
        return usuario;
    }

    @Override
    public boolean atualizar(Usuario usuario) {
        return executar(
                "UPDATE usuario SET nome = ?, cpf = ?, telefone = ?, email = ?, senha = ?, endereco = ? "
                        + "WHERE id_usuario = ?",
                usuario.getNome(), usuario.getCpf(), nuloSeVazio(usuario.getTelefone()),
                nuloSeVazio(usuario.getEmail()), usuario.getSenhaHash(), nuloSeVazio(usuario.getEndereco()),
                usuario.getId()) > 0;
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {
        return consultar("SELECT * FROM usuario WHERE cpf = ?", cpf).stream().findFirst();
    }

    public List<Usuario> listarPorTipo(TipoUsuario tipo) {
        return consultar("SELECT * FROM usuario WHERE tipo_usuario = ? ORDER BY nome", tipo);
    }

    public List<Usuario> buscarPorNome(String trecho, TipoUsuario tipo) {
        return consultar("SELECT * FROM usuario WHERE tipo_usuario = ? AND nome LIKE ? ORDER BY nome",
                tipo, "%" + trecho + "%");
    }

    public boolean existeCpf(String cpf, int idIgnorado) {
        return consultarNumero("SELECT COUNT(*) FROM usuario WHERE cpf = ? AND id_usuario <> ?",
                cpf, idIgnorado) > 0;
    }
}
