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
    private static final String CAMINHO_ARQ_CONSULTAS = "consultas.txt";

    public static void salvarConsulta(Consulta consulta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_CONSULTAS, true))) {
            String linha = consulta.getNomePaciente() + "\t" + 
                          consulta.getNomeMedico() + "\t" + 
                          consulta.getData() + "\t" + 
                          consulta.getHorario() + "\t" + 
                          consulta.getStatus() + "\t" + 
                          consulta.getDescricao() + "\t" + 
                          consulta.getAvaliacao() + "\t" + 
                          consulta.getComentarioAvaliacao();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void salvarTodasConsultas(List<Consulta> consultas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQ_CONSULTAS))) {
            for (Consulta c : consultas) {
                String linha = String.join("\t",
                    c.getNomePaciente(),
                    c.getNomeMedico(),
                    c.getData(),
                    c.getHorario(),
                    c.getStatus(),
                    c.getDescricao(),  // Descrição permanece inalterada
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
            Consulta c = todasConsultas.get(i);
            if (c.getNomePaciente().equals(consultaAtualizada.getNomePaciente()) &&
                c.getNomeMedico().equals(consultaAtualizada.getNomeMedico()) &&
                c.getData().equals(consultaAtualizada.getData()) &&
                c.getHorario().equals(consultaAtualizada.getHorario())) {
                
                // Atualiza apenas os campos que podem ser modificados:
                c.setStatus(consultaAtualizada.getStatus());
                c.setAvaliacao(consultaAtualizada.getAvaliacao());
                c.setComentarioAvaliacao(consultaAtualizada.getComentarioAvaliacao());
                break;
            }
        }
        
        salvarTodasConsultas(todasConsultas);
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
                
                // Consulta básica requer 6 campos
                if (dados.length < 6) continue;
                
                Consulta consulta = new Consulta(
                    dados[0], dados[1], dados[2], dados[3], dados[4], dados[5]
                );
                
                // Se tiver avaliação (campo 7)
                if (dados.length >= 7) {
                    try {
                        consulta.setAvaliacao(Integer.parseInt(dados[6]));
                    } catch (NumberFormatException e) {
                        consulta.setAvaliacao(0);
                    }
                }
                
                // Se tiver comentário (campo 8)
                if (dados.length >= 8) {
                    consulta.setComentarioAvaliacao(dados[7]);
                }
                
                consultas.add(consulta);
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

