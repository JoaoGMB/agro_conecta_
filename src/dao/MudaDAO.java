package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Muda;

public class MudaDAO extends AbstractDAO<Muda> {
    @Override
    protected String getTabela() {
        return "muda";
    }

    @Override
    protected String getColunaId() {
        return "id_muda";
    }

    @Override
    protected Muda mapear(ResultSet rs) throws SQLException {
        Muda muda = new Muda();
        muda.setId(rs.getInt("id_muda"));
        muda.setIdPlantio(rs.getInt("id_plantio"));
        muda.setQuantidade(rs.getInt("quantidade"));
        muda.setGenetica(rs.getString("genetica"));
        muda.setValorAquisicao(rs.getDouble("valor_aquisicao"));
        return muda;
    }

    @Override
    public Muda inserir(Muda muda) {
        int id = inserirRetornandoId(
                "INSERT INTO muda (id_plantio, quantidade, genetica, valor_aquisicao) VALUES (?, ?, ?, ?)",
                muda.getIdPlantio(), muda.getQuantidade(), nuloSeVazio(muda.getGenetica()), muda.getValorAquisicao());
        muda.setId(id);
        return muda;
    }

    @Override
    public boolean atualizar(Muda muda) {
        return executar(
                "UPDATE muda SET quantidade = ?, genetica = ?, valor_aquisicao = ? WHERE id_muda = ?",
                muda.getQuantidade(), nuloSeVazio(muda.getGenetica()), muda.getValorAquisicao(), muda.getId()) > 0;
    }

    public List<Muda> listarPorPlantio(int idPlantio) {
        return consultar("SELECT * FROM muda WHERE id_plantio = ? ORDER BY id_muda", idPlantio);
    }
}
