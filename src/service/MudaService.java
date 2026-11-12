package service;

import java.util.List;

import dao.MudaDAO;
import model.Muda;
import model.Produtor;
import util.ValidacaoException;
import util.Validador;

public class MudaService {
    private final MudaDAO mudaDAO;
    private final PlantioService plantioService;

    public MudaService() {
        this.mudaDAO = new MudaDAO();
        this.plantioService = new PlantioService();
    }

    public Muda cadastrarMuda(Produtor produtor, Muda muda) {
        plantioService.buscarDoProdutor(produtor, muda.getIdPlantio());
        validar(muda);
        return mudaDAO.inserir(muda);
    }

    public List<Muda> listarPorPlantio(Produtor produtor, int idPlantio) {
        plantioService.buscarDoProdutor(produtor, idPlantio);
        return mudaDAO.listarPorPlantio(idPlantio);
    }

    public Muda buscarDoProdutor(Produtor produtor, int idMuda) {
        Muda muda = mudaDAO.buscarPorId(idMuda)
                .orElseThrow(() -> new ValidacaoException("Registro de muda #" + idMuda + " não encontrado."));
        plantioService.buscarDoProdutor(produtor, muda.getIdPlantio());
        return muda;
    }

    public void atualizar(Produtor produtor, Muda muda) {
        buscarDoProdutor(produtor, muda.getId());
        validar(muda);
        mudaDAO.atualizar(muda);
    }

    public void excluir(Produtor produtor, int idMuda) {
        buscarDoProdutor(produtor, idMuda);
        mudaDAO.excluir(idMuda);
    }

    private void validar(Muda muda) {
        Validador.exigirPositivo(muda.getQuantidade(), "Quantidade");
        Validador.exigirTexto(muda.getGenetica(), "Genética");
        Validador.exigirTamanhoMaximo(muda.getGenetica(), 150, "Genética");
        Validador.exigirNaoNegativo(muda.getValorAquisicao(), "Valor de aquisição");
    }
}
