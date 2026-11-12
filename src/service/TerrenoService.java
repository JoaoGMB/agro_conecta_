package service;

import java.util.List;

import dao.ColheitaDAO;
import dao.MonitoramentoDAO;
import dao.MudaDAO;
import dao.PlantioDAO;
import dao.TerrenoDAO;
import dao.VendaDAO;
import model.Colheita;
import model.Plantio;
import model.Produtor;
import model.Terreno;
import util.ValidacaoException;
import util.Validador;

public class TerrenoService {
    private final TerrenoDAO terrenoDAO;
    private final PlantioDAO plantioDAO;
    private final MudaDAO mudaDAO;
    private final MonitoramentoDAO monitoramentoDAO;
    private final ColheitaDAO colheitaDAO;
    private final VendaDAO vendaDAO;

    public TerrenoService() {
        this.terrenoDAO = new TerrenoDAO();
        this.plantioDAO = new PlantioDAO();
        this.mudaDAO = new MudaDAO();
        this.monitoramentoDAO = new MonitoramentoDAO();
        this.colheitaDAO = new ColheitaDAO();
        this.vendaDAO = new VendaDAO();
    }

    public Terreno cadastrarTerreno(Produtor produtor, Terreno terreno) {
        terreno.setIdProdutor(produtor.getId());
        validar(terreno);
        return terrenoDAO.inserir(terreno);
    }

    public List<Terreno> listar(Produtor produtor) {
        return terrenoDAO.listarPorProdutor(produtor.getId());
    }

    public Terreno buscarDoProdutor(Produtor produtor, int idTerreno) {
        Terreno terreno = terrenoDAO.buscarPorId(idTerreno)
                .orElseThrow(() -> new ValidacaoException("Terreno #" + idTerreno + " não encontrado."));
        if (terreno.getIdProdutor() != produtor.getId()) {
            throw new ValidacaoException("O terreno #" + idTerreno + " não pertence a você.");
        }
        return terreno;
    }

    public void atualizarDados(Produtor produtor, Terreno terreno) {
        buscarDoProdutor(produtor, terreno.getId());
        terreno.setIdProdutor(produtor.getId());
        validar(terreno);
        terrenoDAO.atualizar(terreno);
    }

    public void excluir(Produtor produtor, int idTerreno) {
        buscarDoProdutor(produtor, idTerreno);
        terrenoDAO.excluir(idTerreno);
    }

    public Terreno consultarHistorico(Produtor produtor, int idTerreno) {
        Terreno terreno = buscarDoProdutor(produtor, idTerreno);
        for (Plantio plantio : plantioDAO.listarPorTerreno(idTerreno)) {
            mudaDAO.listarPorPlantio(plantio.getId()).forEach(plantio::adicionarMuda);
            monitoramentoDAO.listarPorPlantio(plantio.getId()).forEach(plantio::adicionarMonitoramento);
            for (Colheita colheita : colheitaDAO.listarPorPlantio(plantio.getId())) {
                vendaDAO.listarPorColheita(colheita.getId()).forEach(colheita::adicionarVenda);
                plantio.adicionarColheita(colheita);
            }
            terreno.adicionarPlantio(plantio);
        }
        return terreno;
    }

    private void validar(Terreno terreno) {
        Validador.exigirTexto(terreno.getLocalizacao(), "Localização");
        Validador.exigirTamanhoMaximo(terreno.getLocalizacao(), 255, "Localização");
        Validador.exigirPositivo(terreno.getTamanhoHectares(), "Tamanho em hectares");
        Validador.exigirTamanhoMaximo(terreno.getQualidadeSolo(), 100, "Qualidade do solo");
    }
}
