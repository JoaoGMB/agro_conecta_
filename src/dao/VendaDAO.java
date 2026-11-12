package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Venda;
import util.DataUtil;

public class VendaDAO extends AbstractDAO<Venda> {
    @Override
    protected String getTabela() {
        return "venda";
    }

    @Override
    protected String getColunaId() {
        return "id_venda";
    }

    @Override
    protected Venda mapear(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getInt("id_venda"));
        venda.setIdColheita(rs.getInt("id_colheita"));
        venda.setQuantidadeDisponivel(rs.getDouble("quantidade_disponivel"));
        venda.setQuantidadeVendida(rs.getDouble("quantidade_vendida"));
        venda.setValor(rs.getDouble("valor"));
        venda.setDataVenda(DataUtil.deSql(rs.getDate("data_venda")));
        return venda;
    }

    @Override
    public Venda inserir(Venda venda) {
        int id = inserirRetornandoId(
                "INSERT INTO venda (id_colheita, quantidade_disponivel, quantidade_vendida, valor, data_venda) "
                        + "VALUES (?, ?, ?, ?, ?)",
                venda.getIdColheita(), venda.getQuantidadeDisponivel(), venda.getQuantidadeVendida(),
                venda.getValor(), venda.getDataVenda());
        venda.setId(id);
        return venda;
    }

    @Override
    public boolean atualizar(Venda venda) {
        return executar(
                "UPDATE venda SET quantidade_disponivel = ?, quantidade_vendida = ?, valor = ?, data_venda = ? "
                        + "WHERE id_venda = ?",
                venda.getQuantidadeDisponivel(), venda.getQuantidadeVendida(), venda.getValor(),
                venda.getDataVenda(), venda.getId()) > 0;
    }

    public List<Venda> listarPorColheita(int idColheita) {
        return consultar("SELECT * FROM venda WHERE id_colheita = ? ORDER BY data_venda, id_venda", idColheita);
    }

    public double somarVendido(int idColheita, int idVendaIgnorada) {
        return consultarNumero(
                "SELECT COALESCE(SUM(quantidade_vendida), 0) FROM venda WHERE id_colheita = ? AND id_venda <> ?",
                idColheita, idVendaIgnorada);
    }
}
