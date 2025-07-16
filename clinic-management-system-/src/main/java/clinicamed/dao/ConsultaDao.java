package clinicamed.dao;

import clinicamed.model.Consulta;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConsultaDao {
    private static final String CAMINHO_ARQ_CONSULTAS = "consultas.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean salvarConsulta(Consulta consulta) {
        List<Consulta> consultas = carregarConsultas();
        consultas.add(consulta);
        return salvarConsultas(consultas);
    }

    public static List<Consulta> carregarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        Path path = Paths.get(CAMINHO_ARQ_CONSULTAS);

        if (!Files.exists(path)) return consultas;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length >= 7) {
                    try {
                        Consulta consulta = new Consulta(
                            partes[1], // nomePaciente
                            partes[0], // nomeMedico
                            partes[2], partes[3], partes[4], partes[5]);
                        consulta.setAvaliacao(Integer.parseInt(partes[6]));
                        consultas.add(consulta);
                    } catch (Exception e) {
                        System.err.println("Erro ao ler consulta: " + Arrays.toString(partes));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public static boolean salvarConsultas(List<Consulta> consultas) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(CAMINHO_ARQ_CONSULTAS), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Consulta c : consultas) {
                String linha = String.join("\t",
                    c.getNomeMedico(),
                    c.getNomePaciente(),
                    c.getDataString(),
                    c.getHorario(),
                    c.getStatus(),
                    c.getDescricao(),
                    String.valueOf(c.getAvaliacao())
                );
                writer.write(linha);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void atualizarConsulta(Consulta atualizada) {
        List<Consulta> consultas = carregarConsultas();

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            if (c.getNomePaciente().equals(atualizada.getNomePaciente()) &&
                c.getNomeMedico().equals(atualizada.getNomeMedico()) &&
                c.getData().equals(atualizada.getData()) &&
                c.getHorario().equals(atualizada.getHorario())) {

                consultas.set(i, atualizada);
                break;
            }
        }
        salvarConsultas(consultas);
    }

    public static void removerConsultaDoArq(Consulta consulta) {
        List<Consulta> consultas = carregarConsultas();
        consultas.removeIf(c ->
            c.getNomePaciente().equals(consulta.getNomePaciente()) &&
            c.getNomeMedico().equals(consulta.getNomeMedico()) &&
            c.getData().equals(consulta.getData()) &&
            c.getHorario().equals(consulta.getHorario()));
        salvarConsultas(consultas);
    }

    public static int contarConsultasPorData(clinicamed.model.Medico medico, LocalDate data) {
        return (int) carregarConsultas().stream()
            .filter(c -> c.getNomeMedico().equals(medico.getNome()) && c.getData().equals(data))
            .count();
    }

    public static void promoverPacienteDaListaEspera(String nomeMedico, LocalDate data) {
        // Implementação opcional
    }
}
