package view;

import java.util.List;

import model.Aviso;
import model.Produtor;
import service.AvisoService;

public class MenuAvisos extends MenuProdutorBase {
    private final AvisoService avisoService = new AvisoService();

    public MenuAvisos(Console console, Produtor produtor) {
        super(console, produtor);
    }

    @Override
    protected String getTitulo() {
        return "Avisos e pendências";
    }

    @Override
    protected List<String> getOpcoes() {
        return List.of("Ver avisos pendentes", "Ver todos os avisos", "Marcar aviso como concluído",
                "Reabrir aviso", "Criar lembrete", "Editar aviso", "Excluir aviso", "Verificar pendências agora");
    }

    @Override
    protected boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> console.listar("Nenhuma pendência. Tudo em dia!", avisoService.listarPendentes(produtor));
            case 2 -> console.listar("Nenhum aviso.", avisoService.listar(produtor));
            case 3 -> {
                int id = escolherAviso();
                avisoService.concluirAviso(produtor, id);
                console.sucesso("Aviso #" + id + " concluído.");
            }
            case 4 -> {
                int id = escolherAviso();
                avisoService.reabrir(produtor, id);
                console.sucesso("Aviso #" + id + " reaberto.");
            }
            case 5 -> {
                Aviso aviso = avisoService.gerarAviso(produtor, console.lerTexto("Descrição do lembrete"));
                console.sucesso("Lembrete #" + aviso.getId() + " criado.");
            }
            case 6 -> {
                Aviso aviso = avisoService.buscarDoProdutor(produtor, escolherAviso());
                avisoService.atualizarDescricao(produtor, aviso.getId(),
                        console.lerTextoEdicao("Descrição", aviso.getDescricao()));
                console.sucesso("Aviso atualizado.");
            }
            case 7 -> {
                int id = escolherAviso();
                avisoService.buscarDoProdutor(produtor, id);
                if (console.confirmar("Confirma a exclusão do aviso #" + id)) {
                    avisoService.excluir(produtor, id);
                    console.sucesso("Aviso excluído.");
                }
            }
            case 8 -> console.sucesso(avisoService.gerarAvisosAutomaticos(produtor) + " aviso(s) novo(s) gerado(s).");
            default -> { }
        }
        return true;
    }

    private int escolherAviso() {
        console.listar("Nenhum aviso.", avisoService.listar(produtor));
        return console.lerInteiro("Código do aviso");
    }
}
