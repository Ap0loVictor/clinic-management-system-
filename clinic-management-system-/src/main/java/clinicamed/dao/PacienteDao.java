package clinicamed.dao;

import clinicamed.model.Paciente;
import clinicamed.userfactory.PacienteFactory;

import java.io.*;
import java.util.ArrayList;

public class PacienteDao {
    private static final String CAMINHO_ARQ_PACIENTE = "pacientes.txt";
    public static void salvarPaciente(Paciente paciente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_PACIENTE, true))){
            String linha = paciente.getNome() + "\t" + paciente.getSenha() + "\t" + paciente.getIdade() + "\t" + paciente.isTemPlano();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Paciente> carregarPacientes() {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_PACIENTE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                Paciente paciente = PacienteFactory.criarFromDao(dados);
                pacientes.add(paciente);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return pacientes;
    }
    public static Paciente buscarPorNome(String nome) {
    ArrayList<Paciente> pacientes = carregarPacientes();
    for (Paciente p : pacientes) {
        if (p.getNome().equalsIgnoreCase(nome)) {
            return p;
        }
    }
    return null; 
}

}
