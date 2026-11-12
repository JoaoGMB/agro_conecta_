package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.NivelRisco;
import model.RegistroClima;
import model.RegistroDoenca;
import model.RegistroMonitoramento;
import model.RegistroPraga;
import util.ConexaoBD;
import util.DataUtil;
import util.PersistenciaException;

public class MonitoramentoDAO extends AbstractDAO<RegistroMonitoramento> {
    @Override
    protected String getTabela() {
        return "registro_monitoramento";
    }

    @Override
    protected String getColunaId() {
        return "id_monitoramento";
    }

    @Override
    protected String getSqlSelect() {
        return "SELECT registro_monitoramento.*, "
                + "rp.tipo_praga, rp.nivel_infestacao, "
                + "rd.tipo_doenca, rd.nivel_gravidade, "
                + "rc.volume_chuva "
                + "FROM registro_monitoramento "
                + "LEFT JOIN registro_praga rp ON rp.id_monitoramento = registro_monitoramento.id_monitoramento "
                + "LEFT JOIN registro_doenca rd ON rd.id_monitoramento = registro_monitoramento.id_monitoramento "
                + "LEFT JOIN registro_clima rc ON rc.id_monitoramento = registro_monitoramento.id_monitoramento";
    }

    @Override
    protected RegistroMonitoramento mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_monitoramento");
        RegistroMonitoramento registro;
        switch (tipo) {
            case RegistroMonitoramento.TIPO_PRAGA -> {
                RegistroPraga praga = new RegistroPraga();
                praga.setTipoPraga(rs.getString("tipo_praga"));
                praga.setNivelInfestacao(nivel(rs.getString("nivel_infestacao")));
                registro = praga;
            }
            case RegistroMonitoramento.TIPO_DOENCA -> {
                RegistroDoenca doenca = new RegistroDoenca();
                doenca.setTipoDoenca(rs.getString("tipo_doenca"));
                doenca.setNivelGravidade(nivel(rs.getString("nivel_gravidade")));
                registro = doenca;
            }
            case RegistroMonitoramento.TIPO_CLIMA -> {
                RegistroClima clima = new RegistroClima();
                clima.setVolumeChuva(rs.getDouble("volume_chuva"));
                registro = clima;
            }
            default -> throw new SQLException("Tipo de monitoramento desconhecido: " + tipo);
        }
        registro.setId(rs.getInt("id_monitoramento"));
        registro.setIdPlantio(rs.getInt("id_plantio"));
        registro.setDataRegistro(DataUtil.deSql(rs.getDate("data_registro")));
        registro.setObservacao(rs.getString("observacao"));
        return registro;
    }

    @Override
    public RegistroMonitoramento inserir(RegistroMonitoramento registro) {
        try (Connection conexao = ConexaoBD.getConexao()) {
            conexao.setAutoCommit(false);
            try {
                int id = inserirRetornandoId(conexao,
                        "INSERT INTO registro_monitoramento (id_plantio, data_registro, observacao, tipo_monitoramento) "
                                + "VALUES (?, ?, ?, ?)",
                        registro.getIdPlantio(), registro.getDataRegistro(), nuloSeVazio(registro.getObservacao()),
                        registro.getTipoMonitoramento());
                registro.setId(id);
                gravarSubclasse(conexao, registro, true);
                conexao.commit();
                return registro;
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao inserir registro de monitoramento", e);
        }
    }

    @Override
    public boolean atualizar(RegistroMonitoramento registro) {
        try (Connection conexao = ConexaoBD.getConexao()) {
            conexao.setAutoCommit(false);
            try (PreparedStatement ps = conexao.prepareStatement(
                    "UPDATE registro_monitoramento SET data_registro = ?, observacao = ? WHERE id_monitoramento = ?")) {
                preencher(ps, registro.getDataRegistro(), nuloSeVazio(registro.getObservacao()), registro.getId());
                int linhas = ps.executeUpdate();
                if (linhas > 0) {
                    gravarSubclasse(conexao, registro, false);
                }
                conexao.commit();
                return linhas > 0;
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar registro de monitoramento", e);
        }
    }

    public List<RegistroMonitoramento> listarPorPlantio(int idPlantio) {
        return consultar(getSqlSelect() + " WHERE registro_monitoramento.id_plantio = ? "
                + "ORDER BY registro_monitoramento.data_registro DESC", idPlantio);
    }

    private void gravarSubclasse(Connection conexao, RegistroMonitoramento registro, boolean novo)
            throws SQLException {
        String sql;
        Object[] parametros;
        if (registro instanceof RegistroPraga praga) {
            sql = novo
                    ? "INSERT INTO registro_praga (tipo_praga, nivel_infestacao, id_monitoramento) VALUES (?, ?, ?)"
                    : "UPDATE registro_praga SET tipo_praga = ?, nivel_infestacao = ? WHERE id_monitoramento = ?";
            parametros = new Object[] {praga.getTipoPraga(), praga.getNivelInfestacao(), praga.getId()};
        } else if (registro instanceof RegistroDoenca doenca) {
            sql = novo
                    ? "INSERT INTO registro_doenca (tipo_doenca, nivel_gravidade, id_monitoramento) VALUES (?, ?, ?)"
                    : "UPDATE registro_doenca SET tipo_doenca = ?, nivel_gravidade = ? WHERE id_monitoramento = ?";
            parametros = new Object[] {doenca.getTipoDoenca(), doenca.getNivelGravidade(), doenca.getId()};
        } else if (registro instanceof RegistroClima clima) {
            sql = novo
                    ? "INSERT INTO registro_clima (volume_chuva, id_monitoramento) VALUES (?, ?)"
                    : "UPDATE registro_clima SET volume_chuva = ? WHERE id_monitoramento = ?";
            parametros = new Object[] {clima.getVolumeChuva(), clima.getId()};
        } else {
            throw new SQLException("Subclasse de monitoramento não suportada: " + registro.getClass().getSimpleName());
        }
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencher(ps, parametros);
            ps.executeUpdate();
        }
    }

    private static NivelRisco nivel(String valor) {
        return valor == null ? null : NivelRisco.valueOf(valor);
    }
}
