package util;

import java.time.LocalDate;

public final class Validador {
    private Validador() {
    }

    public static void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacaoException("O campo \"" + campo + "\" é obrigatório.");
        }
    }

    public static void exigirTamanhoMaximo(String valor, int maximo, String campo) {
        if (valor != null && valor.length() > maximo) {
            throw new ValidacaoException("O campo \"" + campo + "\" aceita no máximo " + maximo + " caracteres.");
        }
    }

    public static void exigirPositivo(double valor, String campo) {
        if (valor <= 0) {
            throw new ValidacaoException("O campo \"" + campo + "\" deve ser maior que zero.");
        }
    }

    public static void exigirNaoNegativo(double valor, String campo) {
        if (valor < 0) {
            throw new ValidacaoException("O campo \"" + campo + "\" não pode ser negativo.");
        }
    }

    public static void exigirData(LocalDate data, String campo) {
        if (data == null) {
            throw new ValidacaoException("O campo \"" + campo + "\" é obrigatório.");
        }
    }

    public static void exigirNaoFutura(LocalDate data, String campo) {
        if (data != null && data.isAfter(LocalDate.now())) {
            throw new ValidacaoException("O campo \"" + campo + "\" não pode ser uma data futura.");
        }
    }

    public static void exigirEmailValido(String email) {
        if (email != null && !email.isBlank() && !email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new ValidacaoException("E-mail inválido: " + email);
        }
    }
}
