package service;

import java.util.List;

import dao.VendaDAO;
import model.Colheita;
import model.Produtor;
import model.Venda;
import util.ValidacaoException;
import util.Validador;

public class VendaService {
    private final VendaDAO vendaDAO;
    private final ColheitaService colheitaService;

    public VendaService() {
        this.vendaDAO = new VendaDAO();
        this.colheitaService = new ColheitaService();
    }

    public Venda registrarVenda(Produtor produtor, Venda venda) {
        Colheita colheita = colheitaService.buscarDoProdutor(produtor, venda.getIdColheita());
        atualizarQuantidadeDisponivel(venda, colheita, 0);
        validar(venda, colheita);
        return vendaDAO.inserir(venda);
    }

    public List<Venda> listarPorColheita(Produtor produtor, int idColheita) {
        colheitaService.buscarDoProdutor(produtor, idColheita);
        return vendaDAO.listarPorColheita(idColheita);
    }

    public Venda buscarDoProdutor(Produtor produtor, int idVenda) {
        Venda venda = vendaDAO.buscarPorId(idVenda)
                .orElseThrow(() -> new ValidacaoException("Venda #" + idVenda + " não encontrada."));
        colheitaService.buscarDoProdutor(produtor, venda.getIdColheita());
        return venda;
    }

    public void atualizar(Produtor produtor, Venda venda) {
        buscarDoProdutor(produtor, venda.getId());
        Colheita colheita = colheitaService.buscarDoProdutor(produtor, venda.getIdColheita());
        atualizarQuantidadeDisponivel(venda, colheita, venda.getId());
        validar(venda, colheita);
        vendaDAO.atualizar(venda);
    }

    public void excluir(Produtor produtor, int idVenda) {
        buscarDoProdutor(produtor, idVenda);
        vendaDAO.excluir(idVenda);
    }

    private void atualizarQuantidadeDisponivel(Venda venda, Colheita colheita, int idVendaIgnorada) {
        venda.atualizarQuantidadeDisponivel(colheita.getQuantidadeColhida(),
                vendaDAO.somarVendido(colheita.getId(), idVendaIgnorada));
    }

    private void validar(Venda venda, Colheita colheita) {
        Validador.exigirData(venda.getDataVenda(), "Data da venda");
        if (venda.getDataVenda().isBefore(colheita.getDataColheita())) {
            throw new ValidacaoException("A venda não pode ser anterior à data da colheita.");
        }
        Validador.exigirPositivo(venda.getQuantidadeVendida(), "Quantidade vendida");
        Validador.exigirPositivo(venda.getValor(), "Valor por kg");
        if (venda.getQuantidadeVendida() > venda.getQuantidadeDisponivel()) {
            throw new ValidacaoException(String.format(
                    "Quantidade indisponível: restam apenas %.2f kg desta colheita.", venda.getQuantidadeDisponivel()));
        }
    }
}
