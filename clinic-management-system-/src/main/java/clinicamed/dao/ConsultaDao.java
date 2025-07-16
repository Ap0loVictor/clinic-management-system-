package clinicamed.dao;

import clinicamed.model.Consulta;

import clinicamed.model.Medico;
import javafx.scene.control.Alert;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDao {
    private static final String CAMINHO_ARQ_CONSULTAS = "consultas.txt";

    public static boolean salvarConsulta(Consulta consulta) {
        // Primeiro verifica se já existe uma consulta igual
        List<Consulta> consultas = carregarConsultas();

        // Converte a string da data para LocalDate para comparação
        LocalDate dataConsulta;
        try {
            dataConsulta = LocalDate.parse(consulta.getDataString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.err.println("Formato de data inválido na consulta: " + consulta.getDataString());
            return false;
        }

        // Verifica se o médico já tem 3 consultas neste dia
        long consultasNoDia = consultas.stream()
                .filter(c -> c.getNomeMedico().equals(consulta.getNomeMedico()))
                .filter(c -> {
                    try {
                        return LocalDate.parse(c.getDataString(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")).equals(dataConsulta);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .filter(c -> c.getStatus().equals("Marcada"))
                .count();

        if (consultasNoDia >= 3) {
            System.out.println("Médico já tem 3 consultas nesta data: " + consulta.getDataString());
            return false;
        }

        // Se passou na validação, adiciona a nova consulta
        consultas.add(consulta);

        // Salva no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("consultas.txt"))) {
            for (Consulta c : consultas) {
                String linha = String.join("\t",
                        c.getNomeMedico(),
                        c.getNomePaciente(),
                        c.getDataString(),
                        c.getHorario(),
                        c.getStatus(),
                        c.getDescricao(),
                        String.valueOf(c.getAvaliacao()),
                        c.getComentarioAvaliacao()
                );
                writer.write(linha);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar consultas: " + e.getMessage());
            return false;
        }
    }
    private static void salvarTodasConsultas(List<Consulta> consultas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_CONSULTAS))) {
            for (Consulta c : consultas) {
                String linha = String.join("\t",
                        c.getNomePaciente(),
                        c.getNomeMedico(),
                        c.getData().toString(),  // Garante que a data seja salva como string
                        c.getHorario(),
                        c.getStatus(),
                        c.getDescricao(),
                        String.valueOf(c.getAvaliacao()),
                        c.getComentarioAvaliacao()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void atualizarConsulta(Consulta consultaAtualizada) {
        List<Consulta> todasConsultas = carregarConsultas();

        for (int i = 0; i < todasConsultas.size(); i++) {
            Consulta c = todasConsultas.get(i);  // Corrigido de "Consulta" para "Consulta"
            // ... resto do método permanece igual
        }
        salvarTodasConsultas(todasConsultas);
    }
    public static LocalDate encontrarProximaDataDisponivel(Medico medico, LocalDate dataInicio) {
        for (int i = 1; i <= 30; i++) { // tenta até 30 dias à frente
            LocalDate novaData = dataInicio.plusDays(i);
            if (contarConsultasPorData(medico, novaData) < 3) {
                return novaData;
            }
        }
        return null; // não encontrou data com vaga nos próximos 30 dias
    }

    public static List<Consulta> carregarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQ_CONSULTAS);

        if (!arquivo.exists()) {
            return consultas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQ_CONSULTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");

                if (dados.length < 6) continue;

                try {
                    Consulta consulta = new Consulta(
                            dados[0], dados[1], dados[2], dados[3], dados[4], dados[5]
                    );

                    if (dados.length >= 7) {
                        try {
                            consulta.setAvaliacao(Integer.parseInt(dados[6]));
                        } catch (NumberFormatException e) {
                            consulta.setAvaliacao(0);
                        }
                    }

                    if (dados.length >= 8) {
                        consulta.setComentarioAvaliacao(dados[7]);
                    }

                    consultas.add(consulta);
                } catch (DateTimeParseException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public static int contarConsultasPorData(Medico medico, LocalDate data) {
        List<Consulta> consultas = carregarConsultas();
        return (int) consultas.stream()
                .filter(c -> c.getNomeMedico().equals(medico.getNome()))
                .filter(c -> {
                    try {
                        // Converte a string da consulta para LocalDate
                        LocalDate dataConsulta = LocalDate.parse(c.getDataString(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        return dataConsulta.equals(data);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .filter(c -> c.getStatus().equals("Marcada")) // Só conta consultas marcadas
                .count();
    }

    public static void removerConsultaDoArq(Consulta consulta) {
        Path path = Paths.get(CAMINHO_ARQ_CONSULTAS);
        try {
            List<String> linhas = Files.readAllLines(path);
            List<String> novasLinhas = new ArrayList<>();
            for (String linha : linhas) {
                String[] partes = linha.split("\t");
                if(partes.length >= 6) {
                    String nomePaciente = partes[0].trim();
                    String nomeMedico = partes[1].trim();
                    String dataStr = partes[2].trim();
                    String horario = partes[3].trim();
                    String status = partes[4].trim();
                    String descricao = partes[5].trim();

                    boolean igual = nomeMedico.equals(consulta.getNomeMedico()) &&
                            nomePaciente.equals(consulta.getNomePaciente()) &&
                            dataStr.equals(consulta.getData().toString()) && // Compara como String
                            horario.equals(consulta.getHorario()) &&
                            status.equals(consulta.getStatus()) &&
                            descricao.equals(consulta.getDescricao());
                    if(!igual) {
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

    public static void promoverPacienteDaListaEspera(String medico, LocalDate data) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("lista_espera.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length < 6) continue;

                String medicoEspera = partes[0];
                String paciente = partes[1];
                String dataEsperaStr = partes[2];
                String horario = partes[3];
                String status = partes[4];
                String descricao = partes[5];

                if (!encontrado && medicoEspera.equals(medico) &&
                        LocalDate.parse(dataEsperaStr).equals(data)) {

                    // Promover para consultas.txt
                    Consulta novaConsulta = new Consulta(
                            medico, paciente, data, horario, "Marcada", descricao
                    );
                    salvarConsulta(novaConsulta);
                    encontrado = true;
                } else {
                    linhas.add(linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

