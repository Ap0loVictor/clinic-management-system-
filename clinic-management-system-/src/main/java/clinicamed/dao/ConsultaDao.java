package clinicamed.dao;

import clinicamed.model.Consulta;

import java.io.*;
import java.util.ArrayList;

public class ConsultaDao {
    private static final String CAMINHO_ARQ_CONSULTA = "consultas.txt";

    public static void salvarConsulta(Consulta consulta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_CONSULTA, true))) {
            String linha = consulta.getNomeMedico() + "\t" +
                           consulta.getNomePaciente() + "\t" +
                           consulta.getData() + "\t" +
                           consulta.getHorario() + "\t" +
                           consulta.getStatus() + "\t" +
                           consulta.getDescricao();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Consulta> carregarConsultas() {
        ArrayList<Consulta> consultas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_CONSULTA))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados.length >= 6) {
                    String nomeMedico = dados[0];
                    String nomePaciente = dados[1];
                    String data = dados[2];
                    String horario = dados[3];
                    String status = dados[4];
                    String descricao = dados[5];

                    Consulta consulta = new Consulta(nomeMedico, nomePaciente, data, horario, status, descricao);
                    consultas.add(consulta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultas;
    }
}
