package model;

import util.SenhaUtil;

public abstract class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senhaHash;
    private String endereco;

    protected Usuario() {
    }

    protected Usuario(String nome, String cpf, String telefone, String email, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public abstract TipoUsuario getTipoUsuario();

    public abstract boolean autenticar(String senhaDigitada);

    public abstract String gerarAlerta();

    protected boolean senhaConfere(String senhaDigitada) {
        return senhaDigitada != null
                && senhaHash != null
                && senhaHash.equals(SenhaUtil.gerarHash(senhaDigitada));
    }

    public void definirSenha(String senhaPura) {
        this.senhaHash = SenhaUtil.gerarHash(senhaPura);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | CPF %s | Tel %s | %s | %s",
                id, nome, cpf, vazioSeNulo(telefone), vazioSeNulo(email), vazioSeNulo(endereco));
    }

    protected static String vazioSeNulo(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
