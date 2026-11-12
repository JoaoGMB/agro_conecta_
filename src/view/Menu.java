package view;

import java.util.List;

import util.PersistenciaException;
import util.ValidacaoException;

public abstract class Menu {
    protected final Console console;

    protected Menu(Console console) {
        this.console = console;
    }

    protected abstract String getTitulo();

    protected abstract List<String> getOpcoes();

    protected abstract boolean executarOpcao(int opcao);

    protected String getTextoSair() {
        return "Voltar";
    }

    public void exibir() {
        boolean continuar = true;
        while (continuar) {
            console.titulo(getTitulo());
            List<String> opcoes = getOpcoes();
            for (int i = 0; i < opcoes.size(); i++) {
                console.mensagem((i + 1) + ") " + opcoes.get(i));
            }
            console.mensagem("0) " + getTextoSair());
            int opcao = console.lerOpcao(0, opcoes.size());
            if (opcao == 0) {
                continuar = false;
                continue;
            }
            try {
                continuar = executarOpcao(opcao);
            } catch (ValidacaoException e) {
                console.erro(e.getMessage());
            } catch (PersistenciaException e) {
                console.erro(e.getMessage() + (e.getCause() != null ? " (" + e.getCause().getMessage() + ")" : ""));
            }
        }
    }
}
