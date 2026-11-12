package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Plantio;
import model.StatusPlantio;
import util.DataUtil;

public class PlantioDAO extends AbstractDAO<Plantio> {
    @Override
    protected String getTabela() {
        return "plantio";
    }

    @Override
    protected String getColunaId() {
        return "id_plantio";
    }

    @Override
    protected Plantio mapear(ResultSet rs) throws SQLException {
        Plantio plantio = new Plantio();
        plantio.setId(rs.getInt("id_plantio"));
        plantio.setIdTerreno(rs.getInt("id_terreno"));
        plantio.setDataInicio(DataUtil.deSql(rs.getDate("data_inicio")));
        plantio.setDataTermino(DataUtil.deSql(rs.getDate("data_termino")));
        String status = rs.getString("status");
        plantio.setStatus(status == null ? StatusPlantio.EM_ANDAMENTO : StatusPlantio.valueOf(status));
        plantio.setFotosPlantacao(rs.getString("fotos_plantacao"));
        return plantio;
    }

    @Override
    public Plantio inserir(Plantio plantio) {
        int id = inserirRetornandoId(
                "INSERT INTO plantio (id_terreno, data_inicio, data_termino, status, fotos_plantacao) "
                        + "VALUES (?, ?, ?, ?, ?)",
                plantio.getIdTerreno(), plantio.getDataInicio(), plantio.getDataTermino(), plantio.getStatus(),
                nuloSeVazio(plantio.getFotosPlantacao()));
        plantio.setId(id);
        return plantio;
    }

    @Override
    public boolean atualizar(Plantio plantio) {
        return executar(
                "UPDATE plantio SET id_terreno = ?, data_inicio = ?, data_termino = ?, status = ?, "
                        + "fotos_plantacao = ? WHERE id_plantio = ?",
                plantio.getIdTerreno(), plantio.getDataInicio(), plantio.getDataTermino(), plantio.getStatus(),
                nuloSeVazio(plantio.getFotosPlantacao()), plantio.getId()) > 0;
    }

    public List<Plantio> listarPorTerreno(int idTerreno) {
        return consultar("SELECT * FROM plantio WHERE id_terreno = ? ORDER BY data_inicio DESC", idTerreno);
    }

    public List<Plantio> listarPorProdutor(int idProdutor) {
        return consultar("SELECT p.* FROM plantio p JOIN terreno t ON t.id_terreno = p.id_terreno "
                + "WHERE t.id_produtor = ? ORDER BY p.data_inicio DESC", idProdutor);
    }
}
