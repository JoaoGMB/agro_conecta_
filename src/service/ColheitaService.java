package service;

import java.util.List;

import dao.ColheitaDAO;
import dao.VendaDAO;
import model.Colheita;
import model.Plantio;
import model.Produtor;
import util.ValidacaoException;
import util.Validador;

public class ColheitaService {
    private final ColheitaDAO colheitaDAO;
    private final VendaDAO vendaDAO;
    private final PlantioService plantioService;

    public ColheitaService() {
        this.colheitaDAO = new ColheitaDAO();
        this.vendaDAO = new VendaDAO();
        this.plantioService = new PlantioService();
    }

    public Colheita registrarColheita(Produtor produtor, Colheita colheita) {
        Plantio plantio = plantioService.buscarDoProdutor(produtor, colheita.getIdPlantio());
        validar(colheita, plantio);
        return colheitaDAO.inserir(colheita);
    }

    public List<Colheita> listarPorPlantio(Produtor produtor, int idPlantio) {
        plantioService.buscarDoProdutor(produtor, idPlantio);
        return colheitaDAO.listarPorPlantio(idPlantio);
    }

    public Colheita buscarDoProdutor(Produtor produtor, int idColheita) {
        Colheita colheita = colheitaDAO.buscarPorId(idColheita)
                .orElseThrow(() -> new ValidacaoException("Colheita #" + idColheita + " não encontrada."));
        plantioService.buscarDoProdutor(produtor, colheita.getIdPlantio());
        return colheita;
    }

    public void atualizar(Produtor produtor, Colheita colheita) {
        buscarDoProdutor(produtor, colheita.getId());
        Plantio plantio = plantioService.buscarDoProdutor(produtor, colheita.getIdPlantio());
        validar(colheita, plantio);
        double jaVendido = vendaDAO.somarVendido(colheita.getId(), 0);
        if (colheita.getQuantidadeColhida() < jaVendido) {
            throw new ValidacaoException(String.format(
                    "A quantidade colhida não pode ser menor que o total já vendido (%.2f kg).", jaVendido));
        }
        colheitaDAO.atualizar(colheita);
    }

    public void excluir(Produtor produtor, int idColheita) {
        buscarDoProdutor(produtor, idColheita);
        colheitaDAO.excluir(idColheita);
    }

    public double calcularSaldo(Colheita colheita) {
        return colheita.getQuantidadeColhida() - vendaDAO.somarVendido(colheita.getId(), 0);
    }

    private void validar(Colheita colheita, Plantio plantio) {
        Validador.exigirData(colheita.getDataColheita(), "Data da colheita");
        if (colheita.getDataColheita().isBefore(plantio.getDataInicio())) {
            throw new ValidacaoException("A colheita não pode ser anterior ao início do plantio.");
        }
        Validador.exigirPositivo(colheita.getQuantidadeColhida(), "Quantidade colhida");
        Validador.exigirNaoNegativo(colheita.getTempoSecagem(), "Tempo de secagem");
        Validador.exigirTexto(colheita.getQualidadeProduto(), "Qualidade do produto");
        Validador.exigirTamanhoMaximo(colheita.getQualidadeProduto(), 100, "Qualidade do produto");
    }
}
