package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Colheita;
import util.DataUtil;

public class ColheitaDAO extends AbstractDAO<Colheita> {
    @Override
    protected String getTabela() {
        return "colheita";
    }

    @Override
    protected String getColunaId() {
        return "id_colheita";
    }

    @Override
    protected Colheita mapear(ResultSet rs) throws SQLException {
        Colheita colheita = new Colheita();
        colheita.setId(rs.getInt("id_colheita"));
        colheita.setIdPlantio(rs.getInt("id_plantio"));
        colheita.setDataColheita(DataUtil.deSql(rs.getDate("data_colheita")));
        colheita.setQuantidadeColhida(rs.getDouble("quantidade_colhida"));
        colheita.setTempoSecagem(rs.getDouble("tempo_secagem"));
        colheita.setQualidadeProduto(rs.getString("qualidade_produto"));
        return colheita;
    }

    @Override
    public Colheita inserir(Colheita colheita) {
        int id = inserirRetornandoId(
                "INSERT INTO colheita (id_plantio, data_colheita, quantidade_colhida, tempo_secagem, qualidade_produto) "
                        + "VALUES (?, ?, ?, ?, ?)",
                colheita.getIdPlantio(), colheita.getDataColheita(), colheita.getQuantidadeColhida(),
                colheita.getTempoSecagem(), nuloSeVazio(colheita.getQualidadeProduto()));
        colheita.setId(id);
        return colheita;
    }

    @Override
    public boolean atualizar(Colheita colheita) {
        return executar(
                "UPDATE colheita SET data_colheita = ?, quantidade_colhida = ?, tempo_secagem = ?, "
                        + "qualidade_produto = ? WHERE id_colheita = ?",
                colheita.getDataColheita(), colheita.getQuantidadeColhida(), colheita.getTempoSecagem(),
                nuloSeVazio(colheita.getQualidadeProduto()), colheita.getId()) > 0;
    }

    public List<Colheita> listarPorPlantio(int idPlantio) {
        return consultar("SELECT * FROM colheita WHERE id_plantio = ? ORDER BY data_colheita DESC", idPlantio);
    }
}
