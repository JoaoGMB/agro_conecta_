package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Aviso;
import model.StatusAviso;
import util.DataUtil;

public class AvisoDAO extends AbstractDAO<Aviso> {
    @Override
    protected String getTabela() {
        return "aviso";
    }

    @Override
    protected String getColunaId() {
        return "id_aviso";
    }

    @Override
    protected Aviso mapear(ResultSet rs) throws SQLException {
        Aviso aviso = new Aviso();
        aviso.setId(rs.getInt("id_aviso"));
        aviso.setIdProdutor(rs.getInt("id_produtor"));
        aviso.setDescricao(rs.getString("descricao"));
        aviso.setDataAviso(DataUtil.deSql(rs.getDate("data_aviso")));
        String status = rs.getString("status_conclusao");
        aviso.setStatusConclusao(status == null ? StatusAviso.PENDENTE : StatusAviso.valueOf(status));
        return aviso;
    }

    @Override
    public Aviso inserir(Aviso aviso) {
        int id = inserirRetornandoId(
                "INSERT INTO aviso (id_produtor, descricao, data_aviso, status_conclusao) VALUES (?, ?, ?, ?)",
                aviso.getIdProdutor(), aviso.getDescricao(), aviso.getDataAviso(), aviso.getStatusConclusao());
        aviso.setId(id);
        return aviso;
    }

    @Override
    public boolean atualizar(Aviso aviso) {
        return executar(
                "UPDATE aviso SET descricao = ?, data_aviso = ?, status_conclusao = ? WHERE id_aviso = ?",
                aviso.getDescricao(), aviso.getDataAviso(), aviso.getStatusConclusao(), aviso.getId()) > 0;
    }

    public List<Aviso> listarPorProdutor(int idProdutor) {
        return consultar("SELECT * FROM aviso WHERE id_produtor = ? "
                + "ORDER BY status_conclusao DESC, data_aviso DESC, id_aviso DESC", idProdutor);
    }

    public List<Aviso> listarPendentes(int idProdutor) {
        return consultar("SELECT * FROM aviso WHERE id_produtor = ? AND status_conclusao = ? "
                + "ORDER BY data_aviso DESC, id_aviso DESC", idProdutor, StatusAviso.PENDENTE);
    }

    public boolean existePendente(int idProdutor, String descricao) {
        return consultarNumero("SELECT COUNT(*) FROM aviso WHERE id_produtor = ? AND descricao = ? "
                + "AND status_conclusao = ?", idProdutor, descricao, StatusAviso.PENDENTE) > 0;
    }
}
