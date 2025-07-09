package clinicamed.dao;

import clinicamed.model.Medico;
import clinicamed.userfactory.MedicoFactory;

import java.io.*;
import java.util.ArrayList;

public class MedicoDao {
    private static final String CAMINHO_ARQ_MEDICO = "medicos.txt";
    public static void salvarMedico(Medico medico) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_MEDICO, true))) {
            String linha = medico.getNome() + "\t" + medico.getSenha() + "\t" + medico.getEspecialdiade() + "\t" + medico.getPlanoSaude();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<Medico> carregarMedico() {
        ArrayList<Medico> medicos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_MEDICO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                Medico medico = MedicoFactory.criarFromDao(dados);
                medicos.add(medico);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicos;
    }
}
