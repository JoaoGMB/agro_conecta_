package service;

import java.util.List;

import dao.MonitoramentoDAO;
import model.Plantio;
import model.Produtor;
import model.RegistroDoenca;
import model.RegistroMonitoramento;
import model.RegistroPraga;
import util.ValidacaoException;
import util.Validador;

public class MonitoramentoService {
    private final MonitoramentoDAO monitoramentoDAO;
    private final PlantioService plantioService;
    private final AvisoService avisoService;

    public MonitoramentoService() {
        this.monitoramentoDAO = new MonitoramentoDAO();
        this.plantioService = new PlantioService();
        this.avisoService = new AvisoService();
    }

    public String registrar(Produtor produtor, RegistroMonitoramento registro) {
        Plantio plantio = plantioService.buscarDoProdutor(produtor, registro.getIdPlantio());
        if (!plantio.isEmAndamento()) {
            throw new ValidacaoException("Só é possível monitorar plantios em andamento.");
        }
        validar(registro, plantio);
        monitoramentoDAO.inserir(registro);
        return gerarAvisoSeNecessario(produtor, registro);
    }

    public List<RegistroMonitoramento> listarPorPlantio(Produtor produtor, int idPlantio) {
        plantioService.buscarDoProdutor(produtor, idPlantio);
        return monitoramentoDAO.listarPorPlantio(idPlantio);
    }

    public RegistroMonitoramento buscarDoProdutor(Produtor produtor, int idRegistro) {
        RegistroMonitoramento registro = monitoramentoDAO.buscarPorId(idRegistro)
                .orElseThrow(() -> new ValidacaoException("Registro de monitoramento #" + idRegistro + " não encontrado."));
        plantioService.buscarDoProdutor(produtor, registro.getIdPlantio());
        return registro;
    }

    public String atualizar(Produtor produtor, RegistroMonitoramento registro) {
        RegistroMonitoramento original = buscarDoProdutor(produtor, registro.getId());
        if (!original.getTipoMonitoramento().equals(registro.getTipoMonitoramento())) {
            throw new ValidacaoException("Não é possível trocar o tipo de um registro. Exclua e cadastre outro.");
        }
        Plantio plantio = plantioService.buscarDoProdutor(produtor, registro.getIdPlantio());
        validar(registro, plantio);
        monitoramentoDAO.atualizar(registro);
        return gerarAvisoSeNecessario(produtor, registro);
    }

    public void excluir(Produtor produtor, int idRegistro) {
        buscarDoProdutor(produtor, idRegistro);
        monitoramentoDAO.excluir(idRegistro);
    }

    private String gerarAvisoSeNecessario(Produtor produtor, RegistroMonitoramento registro) {
        String alerta = registro.gerarAlerta();
        if (alerta != null) {
            avisoService.criarSeNaoExistir(produtor.getId(), alerta);
        }
        return alerta;
    }

    private void validar(RegistroMonitoramento registro, Plantio plantio) {
        Validador.exigirData(registro.getDataRegistro(), "Data do registro");
        Validador.exigirNaoFutura(registro.getDataRegistro(), "Data do registro");
        if (registro.getDataRegistro().isBefore(plantio.getDataInicio())) {
            throw new ValidacaoException("A data do registro não pode ser anterior ao início do plantio.");
        }
        if (registro instanceof RegistroPraga praga) {
            Validador.exigirTexto(praga.getTipoPraga(), "Tipo de praga");
            if (praga.getNivelInfestacao() == null) {
                throw new ValidacaoException("Informe o nível de infestação.");
            }
        } else if (registro instanceof RegistroDoenca doenca) {
            Validador.exigirTexto(doenca.getTipoDoenca(), "Tipo de doença");
            if (doenca.getNivelGravidade() == null) {
                throw new ValidacaoException("Informe o nível de gravidade.");
            }
        }
    }
}
