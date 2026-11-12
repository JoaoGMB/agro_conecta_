package app;

import util.ConexaoBD;
import util.PersistenciaException;
import view.Console;
import view.MenuInicial;

public class AgroConectaApp {
    public static void main(String[] args) {
        Console console = new Console();
        try {
            ConexaoBD.testarConexao();
        } catch (PersistenciaException e) {
            console.erro(e.getMessage());
            if (e.getCause() != null) {
                console.erro("Detalhe: " + e.getCause().getMessage());
            }
            console.mensagem("Consulte a seção \"Como executar\" do README.md.");
            System.exit(1);
        }
        new MenuInicial(console).exibir();
        console.mensagem("Até logo!");
    }
}
