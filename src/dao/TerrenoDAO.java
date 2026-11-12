package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Terreno;

public class TerrenoDAO extends AbstractDAO<Terreno> {
    @Override
    protected String getTabela() {
        return "terreno";
    }

    @Override
    protected String getColunaId() {
        return "id_terreno";
    }

    @Override
    protected Terreno mapear(ResultSet rs) throws SQLException {
        Terreno terreno = new Terreno();
        terreno.setId(rs.getInt("id_terreno"));
        terreno.setIdProdutor(rs.getInt("id_produtor"));
        terreno.setLocalizacao(rs.getString("localizacao"));
        terreno.setTamanhoHectares(rs.getDouble("tamanho_hectares"));
        terreno.setQualidadeSolo(rs.getString("qualidade_solo"));
        return terreno;
    }

    @Override
    public Terreno inserir(Terreno terreno) {
        int id = inserirRetornandoId(
                "INSERT INTO terreno (id_produtor, localizacao, tamanho_hectares, qualidade_solo) VALUES (?, ?, ?, ?)",
                terreno.getIdProdutor(), nuloSeVazio(terreno.getLocalizacao()), terreno.getTamanhoHectares(),
                nuloSeVazio(terreno.getQualidadeSolo()));
        terreno.setId(id);
        return terreno;
    }

    @Override
    public boolean atualizar(Terreno terreno) {
        return executar(
                "UPDATE terreno SET localizacao = ?, tamanho_hectares = ?, qualidade_solo = ? WHERE id_terreno = ?",
                nuloSeVazio(terreno.getLocalizacao()), terreno.getTamanhoHectares(),
                nuloSeVazio(terreno.getQualidadeSolo()), terreno.getId()) > 0;
    }

    public List<Terreno> listarPorProdutor(int idProdutor) {
        return consultar("SELECT * FROM terreno WHERE id_produtor = ? ORDER BY id_terreno", idProdutor);
    }
}
