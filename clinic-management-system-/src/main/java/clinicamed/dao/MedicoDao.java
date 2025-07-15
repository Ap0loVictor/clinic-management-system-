package clinicamed.dao;

import clinicamed.model.Medico;
import clinicamed.userfactory.MedicoFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MedicoDao {
    private static final String CAMINHO_ARQ_MEDICO = "medicos.txt";
    
    public static void salvarMedico(Medico medico) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO, true))) {
            String linha = medico.getNome() + "\t" + 
                          medico.getSenha() + "\t" + 
                          medico.getEspecialidade() + "\t" + 
                          medico.getPlanoSaude();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Medico> buscarPorPlano(boolean temPlanoPaciente) {
    ArrayList<Medico> medicos = carregarMedico();
    ArrayList<Medico> filtrados = new ArrayList<>();

    for (Medico m : medicos) {
        if (!temPlanoPaciente) {
            filtrados.add(m);
        } else {
            if (m.getPlanoSaude() != null && !m.getPlanoSaude().equalsIgnoreCase("Não")) {
                filtrados.add(m);
            }
        }
    }
    return filtrados;
}

public static Set<String> listarEspecialidades() {
    Set<String> especialidades = new HashSet<>();
    for (Medico m : carregarMedico()) {
        especialidades.add(m.getEspecialdiade());
    }
    return especialidades;
}

public static ArrayList<Medico> buscarPorEspecialidadeEPlano(String especialidade, boolean temPlanoPaciente) {
    ArrayList<Medico> medicos = carregarMedico();
    ArrayList<Medico> filtrados = new ArrayList<>();

    for (Medico m : medicos) {
        if (!m.getEspecialdiade().equalsIgnoreCase(especialidade)) continue;

        if (!temPlanoPaciente || (m.getPlanoSaude() != null && !m.getPlanoSaude().equalsIgnoreCase("Não"))) {
            filtrados.add(m);
        }
    }
    return filtrados;
}



    public static ArrayList<Medico> carregarMedico() {

        ArrayList<Medico> medicos = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQ_MEDICO);
        
        // Se o arquivo não existir, retorna lista vazia
        if (!arquivo.exists()) {
            return medicos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_MEDICO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados.length >= 4) {
                    Medico medico = MedicoFactory.criarFromDao(dados);
                    medicos.add(medico);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicos;
    }
    
    public static void atualizarMedico(Medico medicoAtualizado) {
        ArrayList<Medico> medicos = carregarMedicos();
        
        // Encontrar e substituir o médico
        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            if (m.getNome().equals(medicoAtualizado.getNome())) {
                medicos.set(i, medicoAtualizado);
                break;
            }
        }
        
        // Reescrever o arquivo completo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO))) {
            for (Medico m : medicos) {
                String linha = m.getNome() + "\t" + 
                             m.getSenha() + "\t" + 
                             m.getEspecialidade() + "\t" + 
                             m.getPlanoSaude();
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Mantenha o método antigo para compatibilidade (se necessário)
    public static ArrayList<Medico> carregarMedico() {
        return carregarMedicos();
    }
}