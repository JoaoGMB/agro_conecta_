package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import dao.AvisoDAO;
import dao.MonitoramentoDAO;
import dao.MudaDAO;
import dao.PlantioDAO;
import dao.TerrenoDAO;
import model.Aviso;
import model.Plantio;
import model.Produtor;
import model.RegistroMonitoramento;
import model.StatusAviso;
import model.Terreno;
import util.ValidacaoException;
import util.Validador;

public class AvisoService {
    public static final int DIAS_SEM_MONITORAMENTO = 7;

    private final AvisoDAO avisoDAO;
    private final TerrenoDAO terrenoDAO;
    private final PlantioDAO plantioDAO;
    private final MudaDAO mudaDAO;
    private final MonitoramentoDAO monitoramentoDAO;

    public AvisoService() {
        this.avisoDAO = new AvisoDAO();
        this.terrenoDAO = new TerrenoDAO();
        this.plantioDAO = new PlantioDAO();
        this.mudaDAO = new MudaDAO();
        this.monitoramentoDAO = new MonitoramentoDAO();
    }

    public int gerarAvisosAutomaticos(Produtor produtor) {
        int criados = 0;
        for (Terreno terreno : terrenoDAO.listarPorProdutor(produtor.getId())) {
            if (terreno.precisaAtualizarDados()) {
                criados += criarSeNaoExistir(produtor.getId(), "Atualize as informações do terreno #"
                        + terreno.getId() + " (localização, tamanho ou qualidade do solo).");
            }
        }
        for (Plantio plantio : plantioDAO.listarPorProdutor(produtor.getId())) {
            if (!plantio.isEmAndamento()) {
                continue;
            }
            if (mudaDAO.listarPorPlantio(plantio.getId()).isEmpty()) {
                criados += criarSeNaoExistir(produtor.getId(),
                        "Registre as mudas utilizadas no plantio #" + plantio.getId() + ".");
            }
            LocalDate ultimoRegistro = monitoramentoDAO.listarPorPlantio(plantio.getId()).stream()
                    .map(RegistroMonitoramento::getDataRegistro)
                    .max(Comparator.naturalOrder())
                    .orElse(plantio.getDataInicio());
            if (ChronoUnit.DAYS.between(ultimoRegistro, LocalDate.now()) > DIAS_SEM_MONITORAMENTO) {
                criados += criarSeNaoExistir(produtor.getId(), "O plantio #" + plantio.getId()
                        + " está há mais de " + DIAS_SEM_MONITORAMENTO
                        + " dias sem monitoramento. Registre pragas, doenças ou chuva.");
            }
        }
        return criados;
    }

    public int criarSeNaoExistir(int idProdutor, String descricao) {
        String texto = descricao.length() > 255 ? descricao.substring(0, 255) : descricao;
        if (avisoDAO.existePendente(idProdutor, texto)) {
            return 0;
        }
        avisoDAO.inserir(new Aviso(idProdutor, texto, LocalDate.now()));
        return 1;
    }

    public Aviso gerarAviso(Produtor produtor, String descricao) {
        Validador.exigirTexto(descricao, "Descrição");
        Validador.exigirTamanhoMaximo(descricao, 255, "Descrição");
        return avisoDAO.inserir(new Aviso(produtor.getId(), descricao.trim(), LocalDate.now()));
    }

    public List<Aviso> listar(Produtor produtor) {
        return avisoDAO.listarPorProdutor(produtor.getId());
    }

    public List<Aviso> listarPendentes(Produtor produtor) {
        return avisoDAO.listarPendentes(produtor.getId());
    }

    public Aviso buscarDoProdutor(Produtor produtor, int idAviso) {
        Aviso aviso = avisoDAO.buscarPorId(idAviso)
                .orElseThrow(() -> new ValidacaoException("Aviso #" + idAviso + " não encontrado."));
        if (aviso.getIdProdutor() != produtor.getId()) {
            throw new ValidacaoException("O aviso #" + idAviso + " não pertence a você.");
        }
        return aviso;
    }

    public void concluirAviso(Produtor produtor, int idAviso) {
        Aviso aviso = buscarDoProdutor(produtor, idAviso);
        aviso.concluirAviso();
        avisoDAO.atualizar(aviso);
    }

    public void reabrir(Produtor produtor, int idAviso) {
        Aviso aviso = buscarDoProdutor(produtor, idAviso);
        aviso.setStatusConclusao(StatusAviso.PENDENTE);
        avisoDAO.atualizar(aviso);
    }

    public void atualizarDescricao(Produtor produtor, int idAviso, String descricao) {
        Aviso aviso = buscarDoProdutor(produtor, idAviso);
        Validador.exigirTexto(descricao, "Descrição");
        Validador.exigirTamanhoMaximo(descricao, 255, "Descrição");
        aviso.setDescricao(descricao.trim());
        avisoDAO.atualizar(aviso);
    }

    public void excluir(Produtor produtor, int idAviso) {
        buscarDoProdutor(produtor, idAviso);
        avisoDAO.excluir(idAviso);
    }
}
