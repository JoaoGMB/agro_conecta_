package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import util.DataUtil;
import util.ValidacaoException;

public class Console {
    private final BufferedReader entrada;
    private final PrintStream saida;

    public Console() {
        this.entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        this.saida = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void titulo(String texto) {
        String linha = "=".repeat(Math.max(40, texto.length() + 4));
        saida.println();
        saida.println(linha);
        saida.println("  " + texto);
        saida.println(linha);
    }

    public void mensagem(String texto) {
        saida.println(texto);
    }

    public void sucesso(String texto) {
        saida.println("[OK] " + texto);
    }

    public void erro(String texto) {
        saida.println("[ERRO] " + texto);
    }

    public void alerta(String texto) {
        saida.println("[ALERTA] " + texto);
    }

    public <T> void listar(String vazio, List<T> itens) {
        if (itens.isEmpty()) {
            saida.println(vazio);
            return;
        }
        itens.forEach(item -> saida.println("  " + item));
    }

    public String lerLinha(String rotulo) {
        saida.print(rotulo);
        saida.flush();
        try {
            String linha = entrada.readLine();
            if (linha == null) {
                saida.println();
                saida.println("Entrada encerrada. Até logo!");
                System.exit(0);
            }
            return linha.trim();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler o teclado", e);
        }
    }

    public String lerTexto(String rotulo) {
        while (true) {
            String valor = lerLinha(rotulo + ": ");
            if (!valor.isEmpty()) {
                return valor;
            }
            erro("Campo obrigatório.");
        }
    }

    public String lerTextoOpcional(String rotulo) {
        return lerLinha(rotulo + " (opcional): ");
    }

    public String lerTextoEdicao(String rotulo, String atual) {
        String valor = lerLinha(rotulo + " [" + (atual == null ? "" : atual) + "]: ");
        return valor.isEmpty() ? atual : valor;
    }

    public int lerInteiro(String rotulo) {
        return lerConvertido(rotulo + ": ", null, Integer::parseInt, "Digite um número inteiro.");
    }

    public int lerInteiroEdicao(String rotulo, int atual) {
        return lerConvertido(rotulo + " [" + atual + "]: ", atual, Integer::parseInt, "Digite um número inteiro.");
    }

    public double lerDecimal(String rotulo) {
        return lerConvertido(rotulo + ": ", null, Console::converterDecimal, "Digite um número (ex.: 12,5).");
    }

    public double lerDecimalEdicao(String rotulo, double atual) {
        return lerConvertido(rotulo + " [" + formatarDecimal(atual) + "]: ", atual,
                Console::converterDecimal, "Digite um número (ex.: 12,5).");
    }

    public LocalDate lerData(String rotulo) {
        LocalDate hoje = LocalDate.now();
        return lerConvertido(rotulo + " (dd/mm/aaaa) [" + DataUtil.formatar(hoje) + "]: ", hoje,
                DataUtil::converter, "Use o formato dd/mm/aaaa.");
    }

    public LocalDate lerDataEdicao(String rotulo, LocalDate atual) {
        return lerConvertido(rotulo + " (dd/mm/aaaa) [" + DataUtil.formatar(atual) + "]: ", atual,
                DataUtil::converter, "Use o formato dd/mm/aaaa.");
    }

    public int lerOpcao(int minimo, int maximo) {
        while (true) {
            String valor = lerLinha("Escolha uma opção: ");
            try {
                int opcao = Integer.parseInt(valor);
                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
            } catch (NumberFormatException ignorada) {
            }
            erro("Opção inválida. Digite um número de " + minimo + " a " + maximo + ".");
        }
    }

    public <E extends Enum<E>> E lerEnum(String rotulo, E[] valores) {
        saida.println(rotulo + ":");
        for (int i = 0; i < valores.length; i++) {
            saida.println("  " + (i + 1) + ") " + valores[i].name());
        }
        return valores[lerOpcao(1, valores.length) - 1];
    }

    public boolean confirmar(String pergunta) {
        while (true) {
            String valor = lerLinha(pergunta + " (s/n): ").toLowerCase();
            if (valor.equals("s") || valor.equals("sim")) {
                return true;
            }
            if (valor.equals("n") || valor.equals("nao") || valor.equals("não")) {
                return false;
            }
            erro("Responda com s ou n.");
        }
    }

    private <T> T lerConvertido(String rotulo, T padrao, Function<String, T> conversor, String mensagemErro) {
        while (true) {
            String valor = lerLinha(rotulo);
            if (valor.isEmpty() && padrao != null) {
                return padrao;
            }
            try {
                return conversor.apply(valor);
            } catch (NumberFormatException | ValidacaoException e) {
                erro(mensagemErro);
            }
        }
    }

    private static double converterDecimal(String texto) {
        return Double.parseDouble(normalizarDecimal(texto));
    }

    private static String normalizarDecimal(String texto) {
        String t = texto.trim();
        if (t.contains(",")) {
            return t.replace(".", "").replace(',', '.');
        }
        return t;
    }

    private static String formatarDecimal(double valor) {
        return String.format("%.2f", valor);
    }
}
