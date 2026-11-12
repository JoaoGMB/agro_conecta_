package model;

public class Administrador extends Usuario {
    public static final int TAMANHO_MINIMO_SENHA = 8;

    public Administrador() {
        super();
    }

    public Administrador(String nome, String cpf, String telefone, String email, String endereco) {
        super(nome, cpf, telefone, email, endereco);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.ADMINISTRADOR;
    }

    @Override
    public boolean autenticar(String senhaDigitada) {
        return senhaDigitada != null
                && senhaDigitada.length() >= TAMANHO_MINIMO_SENHA
                && senhaConfere(senhaDigitada);
    }

    @Override
    public String gerarAlerta() {
        return "Modo administrador: as alterações feitas aqui afetam todos os produtores.";
    }
}
