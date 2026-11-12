package view;

import java.util.List;

import model.NivelRisco;
import model.Produtor;
import model.RegistroClima;
import model.RegistroDoenca;
import model.RegistroMonitoramento;
import model.RegistroPraga;
import service.MonitoramentoService;

public class MenuMonitoramento extends MenuProdutorBase {
    private final MonitoramentoService monitoramentoService = new MonitoramentoService();

    public MenuMonitoramento(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Monitoramento do plantio";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Ver registros de um plantio", "Registrar praga", "Registrar doença",
                "Registrar volume de chuva", "Editar registro", "Excluir registro");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> listar();
            case 2 -> registrar(new RegistroPraga());
            case 3 -> registrar(new RegistroDoenca());
            case 4 -> registrar(new RegistroClima());
            case 5 -> editar();
            case 6 -> excluir();
            default -> { }
        }
        return true;
    }

    private void listar() {
        int idPlantio = escolherPlantio();
        console.listar("Nenhum registro de monitoramento neste plantio.",
                monitoramentoService.listarPorPlantio(produtor, idPlantio));
    }

    private void registrar(RegistroMonitoramento registro) {
        registro.setIdPlantio(escolherPlantio());
        registro.setDataRegistro(console.lerData("Data da observação"));
        preencherCamposEspecificos(registro, false);
        registro.setObservacao(console.lerTextoOpcional("Observação"));
        String alerta = monitoramentoService.registrar(produtor, registro);
        console.sucesso("Registro #" + registro.getId() + " salvo.");
        mostrarAlerta(alerta);
    }

    private void editar() {
        listar();
        RegistroMonitoramento registro =
                monitoramentoService.buscarDoProdutor(produtor, console.lerInteiro("Código do registro"));
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        registro.setDataRegistro(console.lerDataEdicao("Data da observação", registro.getDataRegistro()));
        preencherCamposEspecificos(registro, true);
        registro.setObservacao(console.lerTextoEdicao("Observação", registro.getObservacao()));
        String alerta = monitoramentoService.atualizar(produtor, registro);
        console.sucesso("Registro atualizado.");
        mostrarAlerta(alerta);
    }

    private void excluir() {
        listar();
        int id = console.lerInteiro("Código do registro");
        monitoramentoService.buscarDoProdutor(produtor, id);
        if (console.confirmar("Confirma a exclusão do registro #" + id)) {
            monitoramentoService.excluir(produtor, id);
            console.sucesso("Registro excluído.");
        }
    }

    private void preencherCamposEspecificos(RegistroMonitoramento registro, boolean edicao) {
        if (registro instanceof RegistroPraga praga) {
            praga.setTipoPraga(edicao
                    ? console.lerTextoEdicao("Praga", praga.getTipoPraga())
                    : console.lerTexto("Praga encontrada (ex.: lagarta, pulgão)"));
            praga.setNivelInfestacao(console.lerEnum("Nível de infestação", NivelRisco.values()));
        } else if (registro instanceof RegistroDoenca doenca) {
            doenca.setTipoDoenca(edicao
                    ? console.lerTextoEdicao("Doença", doenca.getTipoDoenca())
                    : console.lerTexto("Doença encontrada (ex.: ferrugem, mancha-de-folha)"));
            doenca.setNivelGravidade(console.lerEnum("Nível de gravidade", NivelRisco.values()));
        } else if (registro instanceof RegistroClima clima) {
            clima.setVolumeChuva(edicao
                    ? console.lerDecimalEdicao("Volume de chuva (mm)", clima.getVolumeChuva())
                    : console.lerDecimal("Volume de chuva (mm)"));
        }
    }

    private void mostrarAlerta(String alerta) {
        if (alerta != null) {
            console.alerta(alerta);
            console.mensagem("Um aviso foi adicionado à sua lista de pendências.");
        }
    }
}
