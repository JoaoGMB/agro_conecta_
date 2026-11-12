package service;

import java.time.LocalDate;
import java.util.List;

import dao.PlantioDAO;
import model.Plantio;
import model.Produtor;
import model.StatusPlantio;
import util.ValidacaoException;
import util.Validador;

public class PlantioService {
    private final PlantioDAO plantioDAO;
    private final TerrenoService terrenoService;

    public PlantioService() {
        this.plantioDAO = new PlantioDAO();
        this.terrenoService = new TerrenoService();
    }

    public Plantio iniciarPlantio(Produtor produtor, Plantio plantio) {
        terrenoService.buscarDoProdutor(produtor, plantio.getIdTerreno());
        plantio.iniciarPlantio();
        validar(plantio);
        return plantioDAO.inserir(plantio);
    }

    public List<Plantio> listar(Produtor produtor) {
        return plantioDAO.listarPorProdutor(produtor.getId());
    }

    public List<Plantio> listarPorTerreno(Produtor produtor, int idTerreno) {
        terrenoService.buscarDoProdutor(produtor, idTerreno);
        return plantioDAO.listarPorTerreno(idTerreno);
    }

    public Plantio buscarDoProdutor(Produtor produtor, int idPlantio) {
        Plantio plantio = plantioDAO.buscarPorId(idPlantio)
                .orElseThrow(() -> new ValidacaoException("Plantio #" + idPlantio + " não encontrado."));
        terrenoService.buscarDoProdutor(produtor, plantio.getIdTerreno());
        return plantio;
    }

    public void atualizar(Produtor produtor, Plantio plantio) {
        buscarDoProdutor(produtor, plantio.getId());
        terrenoService.buscarDoProdutor(produtor, plantio.getIdTerreno());
        if (plantio.getStatus() == StatusPlantio.EM_ANDAMENTO) {
            plantio.setDataTermino(null);
        }
        validar(plantio);
        plantioDAO.atualizar(plantio);
    }

    public void finalizarPlantio(Produtor produtor, int idPlantio, LocalDate dataTermino) {
        Plantio plantio = buscarDoProdutor(produtor, idPlantio);
        if (!plantio.isEmAndamento()) {
            throw new ValidacaoException("O plantio #" + idPlantio + " já está " + plantio.getStatus().getDescricao() + ".");
        }
        plantio.finalizarPlantio(dataTermino == null ? LocalDate.now() : dataTermino);
        validar(plantio);
        plantioDAO.atualizar(plantio);
    }

    public void excluir(Produtor produtor, int idPlantio) {
        buscarDoProdutor(produtor, idPlantio);
        plantioDAO.excluir(idPlantio);
    }

    private void validar(Plantio plantio) {
        Validador.exigirData(plantio.getDataInicio(), "Data de início");
        Validador.exigirTamanhoMaximo(plantio.getFotosPlantacao(), 500, "Fotos da plantação");
        if (plantio.getDataTermino() != null && plantio.getDataTermino().isBefore(plantio.getDataInicio())) {
            throw new ValidacaoException("A data de término não pode ser anterior à data de início.");
        }
        if (plantio.getStatus() == StatusPlantio.FINALIZADO && plantio.getDataTermino() == null) {
            throw new ValidacaoException("Um plantio finalizado precisa da data de término.");
        }
    }
}
