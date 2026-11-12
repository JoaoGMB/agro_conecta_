package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexaoBD {
    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/agroconecta?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO_PADRAO = "agroconecta";
    private static final String SENHA_PADRAO = "agro123";

    private static String url;
    private static String usuario;
    private static String senha;

    private ConexaoBD() {
    }

    public static Connection getConexao() {
        carregarConfiguracao();
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Não foi possível conectar ao banco em " + url
                            + ". Verifique se o MySQL está rodando e se config/db.properties está correto.", e);
        }
    }

    public static void testarConexao() {
        try (Connection conexao = getConexao()) {
            if (!conexao.isValid(5)) {
                throw new PersistenciaException("A conexão com o banco não respondeu.", null);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao testar a conexão com o banco.", e);
        }
    }

    public static String getUrl() {
        carregarConfiguracao();
        return url;
    }

    private static synchronized void carregarConfiguracao() {
        if (url != null) {
            return;
        }
        Properties propriedades = new Properties();
        Path arquivo = Paths.get(System.getProperty("agroconecta.config", "config/db.properties"));
        if (Files.exists(arquivo)) {
            try (InputStream entrada = Files.newInputStream(arquivo)) {
                propriedades.load(entrada);
            } catch (IOException e) {
                throw new PersistenciaException("Erro ao ler o arquivo " + arquivo, e);
            }
        }
        url = escolher(System.getenv("AGRO_DB_URL"), propriedades.getProperty("db.url"), URL_PADRAO);
        usuario = escolher(System.getenv("AGRO_DB_USER"), propriedades.getProperty("db.usuario"), USUARIO_PADRAO);
        senha = escolher(System.getenv("AGRO_DB_PASSWORD"), propriedades.getProperty("db.senha"), SENHA_PADRAO);
    }

    private static String escolher(String ambiente, String arquivo, String padrao) {
        if (ambiente != null) {
            return ambiente;
        }
        if (arquivo != null) {
            return arquivo.trim();
        }
        return padrao;
    }
}
