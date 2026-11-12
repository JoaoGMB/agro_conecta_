package util;

public final class CpfUtil {
    private CpfUtil() {
    }

    public static String somenteDigitos(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    public static String formatar(String cpf) {
        String d = somenteDigitos(cpf);
        if (d.length() != 11) {
            return cpf == null ? null : cpf.trim();
        }
        return d.substring(0, 3) + "." + d.substring(3, 6) + "." + d.substring(6, 9) + "-" + d.substring(9);
    }

    public static boolean isValido(String cpf) {
        String d = somenteDigitos(cpf);
        if (d.length() != 11 || d.chars().distinct().count() == 1) {
            return false;
        }
        return calcularDigito(d, 9) == d.charAt(9) - '0'
                && calcularDigito(d, 10) == d.charAt(10) - '0';
    }

    private static int calcularDigito(String digitos, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (digitos.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
