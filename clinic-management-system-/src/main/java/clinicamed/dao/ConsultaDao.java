package clinicamed.dao;

import clinicamed.model.Consulta;
import clinicamed.model.Medico;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    }
    public static void removerConsultaDoArq(Consulta consulta) {
        Path path = Paths.get("consultas.txt");
        try {
            List<String> linhas = Files.readAllLines(path);
            List<String> novasLinhas = new ArrayList<>();
            for (String linha : linhas) {
                String[] partes = linha.split("\t");
                if(partes.length == 6) {
                    String nomeMedico = partes[0].trim();
                    String nomePaciente = partes[1].trim();
                    String data = partes[2].trim();
                    String horario = partes[3].trim();
                    String status = partes[4].trim();
                    String descricao = partes[5].trim();
                    boolean eigual = nomeMedico.equals(consulta.getNomeMedico()) &&
                            nomePaciente.equals(consulta.getNomePaciente()) &&
                            data.equals(consulta.getData()) && horario.equals(consulta.getHorario()) &&
                            status.equals(consulta.getStatus()) && descricao.equals(consulta.getDescricao());
                    if(!eigual) {
                        novasLinhas.add(linha);
                    }
                }
            }
            Files.write(path, novasLinhas);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao cancelar consulta");
            alert.setContentText("Não foi possível atualizar o arquivo de consultas.");
            alert.showAndWait();
        }
    }
    public static void promoverPacienteDaListaEspera(String medico, String data) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("lista_espera.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length < 6) continue;

                String medicoEspera = partes[0];
                String paciente = partes[1];
                String dataEspera = partes[2];
                String horario = partes[3];
                String status = partes[4];
                String descricao = partes[5];

                if (!encontrado && medicoEspera.equals(medico) && dataEspera.equals(data)) {
                    // Promover para consultas.txt
                    Consulta novaConsulta = new Consulta(medico, paciente, data, horario, "Marcada", descricao);
                    salvarConsulta(novaConsulta);
                    encontrado = true; // só promove o primeiro da fila
                } else {
                    linhas.add(linha); // mantém na lista os demais
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reescreve a lista_espera.txt sem a linha promovida
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lista_espera.txt"))) {
            for (String l : linhas) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
