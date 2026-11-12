package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Produtor extends Usuario {
    public static final int TAMANHO_MINIMO_SENHA = 4;

    private final List<Terreno> terrenos = new ArrayList<>();

    public Produtor() {
        super();
    }

    public Produtor(String nome, String cpf, String telefone, String email, String endereco) {
        super(nome, cpf, telefone, email, endereco);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.PRODUTOR;
    }

    @Override
    public boolean autenticar(String senhaDigitada) {
        return senhaConfere(senhaDigitada);
    }

    @Override
    public String gerarAlerta() {
        return "Bem-vindo(a), " + getNome() + "! Confira seus avisos pendentes no menu principal.";
    }

    public void adicionarTerreno(Terreno terreno) {
        terrenos.add(terreno);
    }

    public List<Terreno> getTerrenos() {
        return Collections.unmodifiableList(terrenos);
    }

    public void limparTerrenos() {
        terrenos.clear();
    }

    public double getAreaTotalHectares() {
        return terrenos.stream().mapToDouble(Terreno::getTamanhoHectares).sum();
    }
}
