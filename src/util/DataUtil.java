package util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DataUtil {
    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DataUtil() {
    }

    public static String formatar(LocalDate data) {
        return data == null ? "-" : data.format(FORMATO_BR);
    }

    public static LocalDate converter(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim(), FORMATO_BR);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("Data inválida: \"" + texto + "\". Use o formato dd/mm/aaaa.");
        }
    }

    public static Date paraSql(LocalDate data) {
        return data == null ? null : Date.valueOf(data);
    }

    public static LocalDate deSql(Date data) {
        return data == null ? null : data.toLocalDate();
    }
}
