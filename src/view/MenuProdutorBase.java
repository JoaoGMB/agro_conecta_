package view;

import java.util.List;

import model.Colheita;
import model.Plantio;
import model.Produtor;
import model.Terreno;
import service.ColheitaService;
import service.PlantioService;
import service.TerrenoService;
import util.ValidacaoException;

public abstract class MenuProdutorBase extends Menu {
    protected final Produtor produtor;
    protected final TerrenoService terrenoService = new TerrenoService();
    protected final PlantioService plantioService = new PlantioService();
    protected final ColheitaService colheitaService = new ColheitaService();

    protected MenuProdutorBase(Console console, Produtor produtor) {
        super(console);
        this.produtor = produtor;
    }

    protected int escolherTerreno() {
        List<Terreno> terrenos = terrenoService.listar(produtor);
        if (terrenos.isEmpty()) {
            throw new ValidacaoException("Você ainda não tem terrenos. Cadastre um no menu Terrenos.");
        }
        console.mensagem("Seus terrenos:");
        console.listar("", terrenos);
        return console.lerInteiro("Código do terreno");
    }

    protected int escolherPlantio() {
        List<Plantio> plantios = plantioService.listar(produtor);
        if (plantios.isEmpty()) {
            throw new ValidacaoException("Você ainda não tem plantios. Inicie um no menu Plantios.");
        }
        console.mensagem("Seus plantios:");
        console.listar("", plantios);
        return console.lerInteiro("Código do plantio");
    }

    protected int escolherColheita() {
        int idPlantio = escolherPlantio();
        List<Colheita> colheitas = colheitaService.listarPorPlantio(produtor, idPlantio);
        if (colheitas.isEmpty()) {
            throw new ValidacaoException("O plantio #" + idPlantio + " ainda não tem colheitas registradas.");
        }
        console.mensagem("Colheitas do plantio #" + idPlantio + ":");
        for (Colheita colheita : colheitas) {
            console.mensagem(String.format("  %s | Saldo para venda: %.2f kg",
                    colheita, colheitaService.calcularSaldo(colheita)));
        }
        return console.lerInteiro("Código da colheita");
    }
}
