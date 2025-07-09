package clinicamed.userfactory;

import clinicamed.model.Paciente;

public class PacienteFactory {
    public static Paciente criarFromDao(String[] dados) {
        String nome = dados[0];
        String senha = dados[1];
        int idade = Integer.parseInt(dados[2]);
        boolean temPlano = Boolean.parseBoolean(dados[3]);

        return new Paciente(nome, senha, idade, temPlano);
    }
    public static Paciente criarFromController(String nome, String senha, int idade, boolean temPlano) {
        return new Paciente(nome, senha, idade, temPlano);
    }
}
