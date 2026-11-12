package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import util.ConexaoBD;
import util.DataUtil;
import util.PersistenciaException;

public abstract class AbstractDAO<T> implements GenericDAO<T> {
    protected abstract String getTabela();

    protected abstract String getColunaId();

    protected abstract T mapear(ResultSet rs) throws SQLException;

    protected String getSqlSelect() {
        return "SELECT * FROM " + getTabela();
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return consultar(getSqlSelect() + " WHERE " + getTabela() + "." + getColunaId() + " = ?", id)
                .stream().findFirst();
    }

    @Override
    public List<T> listarTodos() {
        return consultar(getSqlSelect() + " ORDER BY " + getTabela() + "." + getColunaId());
    }

    @Override
    public boolean excluir(int id) {
        return executar("DELETE FROM " + getTabela() + " WHERE " + getColunaId() + " = ?", id) > 0;
    }

    protected List<T> consultar(String sql, Object... parametros) {
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencher(ps, parametros);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar " + getTabela(), e);
        }
    }

    protected double consultarNumero(String sql, Object... parametros) {
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencher(ps, parametros);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar " + getTabela(), e);
        }
    }

    protected int executar(String sql, Object... parametros) {
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencher(ps, parametros);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao gravar em " + getTabela(), e);
        }
    }

    protected int inserirRetornandoId(String sql, Object... parametros) {
        try (Connection conexao = ConexaoBD.getConexao()) {
            return inserirRetornandoId(conexao, sql, parametros);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir em " + getTabela(), e);
        }
    }

    protected int inserirRetornandoId(Connection conexao, String sql, Object... parametros) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, parametros);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
                throw new SQLException("O banco não devolveu o id gerado em " + getTabela());
            }
        }
    }

    protected static void preencher(PreparedStatement ps, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            Object valor = parametros[i];
            int posicao = i + 1;
            if (valor == null) {
                ps.setNull(posicao, Types.NULL);
            } else if (valor instanceof LocalDate data) {
                ps.setDate(posicao, DataUtil.paraSql(data));
            } else if (valor instanceof Enum<?> constante) {
                ps.setString(posicao, constante.name());
            } else {
                ps.setObject(posicao, valor);
            }
        }
    }

    protected static String nuloSeVazio(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
