package clinicamed.dao;

import clinicamed.model.Consulta;
import clinicamed.model.Medico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static int contarConsultasPorData(Medico medico, String data) {
        int contador = 0;
        List<Consulta> consultas = carregarConsultas();

        for (Consulta c : consultas) {
            if (c.getNomeMedico().equals(medico.getNome()) && c.getData().equals(data) && !c.getStatus().equals("Lista de Espera")) {
                contador++;
            }
        }

        return contador;
    }

    public static void atualizarConsulta(Consulta consultaAtualizada) {
        List<Consulta> consultas = carregarConsultas();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_CONSULTA))) {
            for (Consulta c : consultas) {
                if (c.getNomeMedico().equals(consultaAtualizada.getNomeMedico()) &&
                    c.getNomePaciente().equals(consultaAtualizada.getNomePaciente()) &&
                    c.getData().equals(consultaAtualizada.getData()) &&
                    c.getHorario().equals(consultaAtualizada.getHorario())) {
                    c = consultaAtualizada; // atualiza a consulta
                }

                String linha = c.getNomeMedico() + "\t" +
                               c.getNomePaciente() + "\t" +
                               c.getData() + "\t" +
                               c.getHorario() + "\t" +
                               c.getStatus() + "\t" +
                               c.getDescricao();
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    /*public static List<Consulta> buscarPorPaciente(String nomePaciente) {
        List<Consulta> todas = carregarConsultas(); // carrega todas do JSON ou banco
        List<Consulta> filtradas = new ArrayList<>();

        for (Consulta c : todas) {
            if (c.getNomePaciente().equalsIgnoreCase(nomePaciente)) {
                filtradas.add(c);
            }
        }

        return filtradas;*/
    }
}
