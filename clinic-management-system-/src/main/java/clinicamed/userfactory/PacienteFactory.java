package clinicamed.userfactory;

import clinicamed.model.Paciente;

public class PacienteFactory {
    public static Paciente criarFromDao(String[] dados) {
        String nome = dados[0];
        String senha = dados[1];
        int idade = Integer.parseInt(dados[2]);
        String planoSaude = dados[3]; // Agora é String

        return new Paciente(nome, senha, idade, planoSaude);
    }
    
    public static Paciente criarFromController(String nome, String senha, int idade, String planoSaude) {
        return new Paciente(nome, senha, idade, planoSaude);
    }
}