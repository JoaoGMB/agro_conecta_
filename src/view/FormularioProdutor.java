package view;

import model.Produtor;

public final class FormularioProdutor {
    private FormularioProdutor() {
    }

    public static Produtor ler(Console console) {
        Produtor produtor = new Produtor();
        produtor.setNome(console.lerTexto("Nome completo"));
        produtor.setCpf(console.lerTexto("CPF"));
        produtor.setTelefone(console.lerTextoOpcional("Telefone"));
        produtor.setEmail(console.lerTextoOpcional("E-mail"));
        produtor.setEndereco(console.lerTextoOpcional("Endereço"));
        return produtor;
    }

    public static void editar(Console console, Produtor produtor) {
        console.mensagem("(Pressione ENTER para manter o valor atual)");
        produtor.setNome(console.lerTextoEdicao("Nome completo", produtor.getNome()));
        produtor.setCpf(console.lerTextoEdicao("CPF", produtor.getCpf()));
        produtor.setTelefone(console.lerTextoEdicao("Telefone", produtor.getTelefone()));
        produtor.setEmail(console.lerTextoEdicao("E-mail", produtor.getEmail()));
        produtor.setEndereco(console.lerTextoEdicao("Endereço", produtor.getEndereco()));
    }
}
