package clinicamed.dao;

import clinicamed.model.Paciente;
import clinicamed.userfactory.PacienteFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacienteDao {
    private static final String CAMINHO_ARQ_PACIENTE = "pacientes.txt";
        private static final List<String> PLANOS_DISPONIVEIS = Arrays.asList(
        "Unimed", "SulAmérica", "Bradesco Saúde", "Amil", "NotreDame", "Porto Seguro"
    );
    // PacienteDao.java
public static void atualizarPaciente(Paciente pacienteAtualizado) {
    ArrayList<Paciente> pacientes = carregarPacientes();
    
    // Encontrar e substituir o paciente
    for (int i = 0; i < pacientes.size(); i++) {
        Paciente p = pacientes.get(i);
        if (p.getNome().equals(pacienteAtualizado.getNome())) {
            pacientes.set(i, pacienteAtualizado);
            break;
        }
    }
    
    // Reescrever o arquivo completo
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_PACIENTE))) {
        for (Paciente p : pacientes) {
            String linha = p.getNome() + "\t" + p.getSenha() + "\t" + 
                          p.getIdade() + "\t" + p.getPlanoSaude();
            writer.write(linha);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static List<String> getPlanosDisponiveis() {
        return new ArrayList<>(PLANOS_DISPONIVEIS);
    }

    public static void salvarPaciente(Paciente paciente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_PACIENTE, true))) {
            String linha = paciente.getNome() + "\t" + 
                          paciente.getSenha() + "\t" + 
                          paciente.getIdade() + "\t" + 
                          paciente.getPlanoSaude();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Paciente> carregarPacientes() {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQ_PACIENTE);
        
        // Se o arquivo não existir, retorna lista vazia
        if (!arquivo.exists()) {
            return pacientes;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_PACIENTE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados.length >= 4) {
                    Paciente paciente = PacienteFactory.criarFromDao(dados);
                    pacientes.add(paciente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
}
