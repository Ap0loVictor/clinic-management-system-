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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO, true))) {
            // Formato: nome\tsenha\tespecialidade\tplanoSaude\tavaliacao
            String linha = medico.getNome() + "\t" +
                    medico.getSenha() + "\t" +
                    medico.getEspecialidade() + "\t" +
                    medico.getPlanoSaude() + "\t" +
                    medico.getAvaliacao();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Medico> buscarPorPlano(boolean temPlanoPaciente) {
        ArrayList<Medico> medicos = carregarMedicos();
        ArrayList<Medico> filtrados = new ArrayList<>();

        for (Medico m : medicos) {
            if (!temPlanoPaciente) {
                filtrados.add(m);
            } else if (m.getPlanoSaude() != null && !m.getPlanoSaude().equalsIgnoreCase("Não")) {
                filtrados.add(m);
            }
        }
        return filtrados;
    }

    public static Set<String> listarEspecialidades() {
        Set<String> especialidades = new HashSet<>();
        for (Medico m : carregarMedicos()) {
            especialidades.add(m.getEspecialidade());
        }
        return especialidades;
    }

    public static ArrayList<Medico> buscarPorEspecialidadeEPlano(String especialidade, boolean temPlanoPaciente) {
        ArrayList<Medico> medicos = carregarMedicos();
        ArrayList<Medico> filtrados = new ArrayList<>();

        for (Medico m : medicos) {
            if (!m.getEspecialidade().equalsIgnoreCase(especialidade)) continue;

            if (!temPlanoPaciente || (m.getPlanoSaude() != null && !m.getPlanoSaude().equalsIgnoreCase("Não"))) {
                filtrados.add(m);
            }
        }
        return filtrados;
    }

    public static ArrayList<Medico> carregarMedicos() {
        ArrayList<Medico> medicos = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQ_MEDICO);

        if (!arquivo.exists()) {
            return medicos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_MEDICO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados.length >= 4) {
                    // Usa a factory para manter compatibilidade
                    Medico medico = MedicoFactory.criarFromDao(dados);
                    
                    // Adiciona avaliação se existir (campo 5)
                    if (dados.length >= 5) {
                        try {
                            double avaliacao = Double.parseDouble(dados[4]);
                            medico.setAvaliacao(avaliacao);
                        } catch (NumberFormatException e) {
                            // Mantém o padrão 0.0 se não conseguir converter
                        }
                    }
                    
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

        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            if (m.getNome().equals(medicoAtualizado.getNome())) {
                medicos.set(i, medicoAtualizado);
                break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO))) {
            for (Medico m : medicos) {
                String linha = m.getNome() + "\t" +
                        m.getSenha() + "\t" +
                        m.getEspecialidade() + "\t" +
                        m.getPlanoSaude() + "\t" +
                        m.getAvaliacao(); // Inclui a avaliação
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método antigo mantido por compatibilidade
    public static ArrayList<Medico> carregarMedico() {
        return carregarMedicos();
    }
    
    // NOVO MÉTODO: Atualiza apenas a avaliação de um médico
    public static void atualizarAvaliacaoMedico(String nomeMedico, double novaAvaliacao) {
        ArrayList<Medico> medicos = carregarMedicos();
        
        for (Medico m : medicos) {
            if (m.getNome().equals(nomeMedico)) {
                // Atualiza a avaliação do médico
                m.setAvaliacao(novaAvaliacao);
                break;
            }
        }
        
        // Reescreve o arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO))) {
            for (Medico m : medicos) {
                String linha = m.getNome() + "\t" +
                        m.getSenha() + "\t" +
                        m.getEspecialidade() + "\t" +
                        m.getPlanoSaude() + "\t" +
                        m.getAvaliacao();
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}