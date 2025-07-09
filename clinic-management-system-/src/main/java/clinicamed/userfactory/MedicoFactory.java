package clinicamed.userfactory;

import clinicamed.model.Medico;

public class MedicoFactory {
    public static Medico criarFromDao(String[] dados) {
        String nome = dados[0];
        String senha = dados[1];
        String especialidade = dados[2];
        String planoSaude = dados[3];

        return new Medico(nome, senha, especialidade, planoSaude);
    }
    public static Medico criarFromController(String nome, String senha, String especialidade, String planoSaude) {
        return new Medico(nome, senha, especialidade, planoSaude);
    }
}
