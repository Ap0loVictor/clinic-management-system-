package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.dao.MedicoDao;
import clinicamed.model.Consulta;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CadastrarConsultaController implements Initializable {

    @FXML private ComboBox<String> comboEspecialidades;
    @FXML private ComboBox<String> comboMedicos;
    @FXML private TextField campoPaciente;
    @FXML private DatePicker campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private Button buttonSalvar;
    @FXML private Button buttonVoltar;
    @FXML private ComboBox<String> comboPlano;

    private Paciente paciente;
    private List<Medico> medicosFiltrados;

    private final DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialização se necessário
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        campoPaciente.setText(paciente.getNome());
        campoPaciente.setEditable(false);

        comboPlano.getItems().clear();
        comboPlano.getItems().add("Não tenho");
        if (paciente.temPlano()) {
            comboPlano.getItems().add(paciente.getPlanoSaude());
            comboPlano.setValue(paciente.getPlanoSaude());
        } else {
            comboPlano.setValue("Não tenho");
        }
        carregarEspecialidades();
    }

    private void carregarEspecialidades() {
        comboEspecialidades.getItems().clear();
        String planoSelecionado = comboPlano.getValue();
        boolean temPlano = planoSelecionado != null && !planoSelecionado.equalsIgnoreCase("Não tenho");

        List<Medico> medicos = MedicoDao.carregarMedicos();

        for (Medico m : medicos) {
            if (!temPlano || m.getPlanoSaude().equalsIgnoreCase(planoSelecionado)) {
                if (!comboEspecialidades.getItems().contains(m.getEspecialidade())) {
                    comboEspecialidades.getItems().add(m.getEspecialidade());
                }
            }
        }
    }

    @FXML
    private void atualizarMedicos() {
        String especialidadeSelecionada = comboEspecialidades.getValue();
        String planoSelecionado = comboPlano.getValue();
        if (especialidadeSelecionada == null || planoSelecionado == null) return;

        comboMedicos.getItems().clear();
        boolean temPlano = !planoSelecionado.equalsIgnoreCase("Não tenho");

        medicosFiltrados = MedicoDao.carregarMedicos().stream()
                .filter(m -> m.getEspecialidade().equalsIgnoreCase(especialidadeSelecionada))
                .filter(m -> !temPlano || m.getPlanoSaude().equalsIgnoreCase(planoSelecionado))
                .collect(Collectors.toList());

        medicosFiltrados.forEach(m -> comboMedicos.getItems().add(m.getNome()));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean mostrarConfirmacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private LocalDate encontrarProximaDataDisponivel(Medico medico, LocalDate dataOriginal) {
        LocalDate dataAtual = dataOriginal.plusDays(1);
        int diasVerificados = 0;
        final int MAX_DIAS = 30;

        while (diasVerificados < MAX_DIAS) {
            if (dataAtual.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    dataAtual.getDayOfWeek() != DayOfWeek.SUNDAY) {

                if (ConsultaDao.contarConsultasPorData(medico, dataAtual) < 3) {
                    return dataAtual;
                }
                diasVerificados++;
            }
            dataAtual = dataAtual.plusDays(1);
        }
        return null;
    }

    @FXML
    private void handleSalvarConsulta() {
        try {
            String nomeMedico = comboMedicos.getValue();
            LocalDate data = campoData.getValue();
            String horarioTexto = campoHorario.getText().trim();
            String descricao = campoDescricao.getText().trim();

            if (nomeMedico == null || data == null || horarioTexto.isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos obrigatórios!");
                return;
            }

            LocalTime horario;
            try {
                horario = LocalTime.parse(horarioTexto, formatterHora);
            } catch (DateTimeParseException e) {
                mostrarAlerta("Erro", "Horário inválido. Use o formato HH:mm (ex: 14:30).");
                return;
            }

            Medico medicoSelecionado = medicosFiltrados.stream()
                    .filter(m -> m.getNome().equals(nomeMedico))
                    .findFirst()
                    .orElse(null);

            if (medicoSelecionado == null) {
                mostrarAlerta("Erro", "Médico não encontrado!");
                return;
            }

            int consultasNoDia = ConsultaDao.contarConsultasPorData(medicoSelecionado, data);

            if (consultasNoDia >= 3) {
                LocalDate novaData = encontrarProximaDataDisponivel(medicoSelecionado, data);

                if (novaData != null) {
                    boolean aceitou = mostrarConfirmacao("Agenda Lotada",
                            "Dr. " + nomeMedico + " já tem 3 consultas em " + data.format(formatterData) +
                                    "\nDeseja agendar para " + novaData.format(formatterData) + "?");

                    if (aceitou) {
                        Consulta consulta = new Consulta(
                                nomeMedico,
                                paciente.getNome(),
                                novaData.format(formatterData),
                                horario.format(formatterHora),
                                "Marcada",
                                descricao
                        );

                        if (ConsultaDao.salvarConsulta(consulta)) {
                            mostrarAlerta("Sucesso", "Consulta marcada para " +
                                    novaData.format(formatterData) + " às " +
                                    horario.format(formatterHora));
                            promoverDaListaEspera(medicoSelecionado);
                            handleVoltar();
                            return;
                        }
                    }
                }

                adicionarListaEspera(
                        paciente.getNome(),
                        nomeMedico,
                        data.format(formatterData),
                        horario.format(formatterHora),
                        "Em espera",
                        descricao
                );

                mostrarAlerta("Lista de Espera",
                        "Você foi adicionado à lista de espera do Dr. " + nomeMedico +
                                "\nEntraremos em contato quando houver disponibilidade.");
                return;
            }

            Consulta consulta = new Consulta(
                    nomeMedico,
                    paciente.getNome(),
                    data.format(formatterData),
                    horario.format(formatterHora),
                    "Marcada",
                    descricao
            );

            if (ConsultaDao.salvarConsulta(consulta)) {
                mostrarAlerta("Sucesso", "Consulta marcada com sucesso para " +
                        data.format(formatterData) + " às " +
                        horario.format(formatterHora));
                promoverDaListaEspera(medicoSelecionado);
                handleVoltar();
            } else {
                mostrarAlerta("Erro", "Não foi possível marcar a consulta. Tente novamente.");
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarListaEspera(String paciente, String medico, String data,
                                      String horario, String status, String descricao) {
        String linha = String.join("\t", medico, paciente, data, horario, status, descricao);

        try {
            Path path = Paths.get("lista_espera.txt");
            if (Files.exists(path)) {
                List<String> linhas = Files.readAllLines(path);
                for (String linhaExistente : linhas) {
                    if (linhaExistente.startsWith(medico + "\t" + paciente + "\t" + data)) {
                        mostrarAlerta("Aviso", "Você já está na lista de espera para esta data.");
                        return;
                    }
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            mostrarAlerta("Erro", "Falha ao acessar a lista de espera: " + e.getMessage());
        }
    }

    private void promoverDaListaEspera(Medico medico) {
        try {
            Path path = Paths.get("lista_espera.txt");
            if (!Files.exists(path)) return;

            List<String> linhas = Files.readAllLines(path);
            List<String> linhasAtualizadas = new ArrayList<>();
            boolean promovido = false;

            for (String linha : linhas) {
                if (promovido) {
                    linhasAtualizadas.add(linha);
                    continue;
                }

                String[] partes = linha.split("\t");
                if (partes.length >= 6 && partes[0].equals(medico.getNome())) {
                    try {
                        LocalDate dataEspera = LocalDate.parse(partes[2], formatterData);

                        if (ConsultaDao.contarConsultasPorData(medico, dataEspera) < 3) {
                            Consulta consulta = new Consulta(
                                    partes[0], partes[1], partes[2],
                                    partes[3], "Marcada", partes[5]
                            );

                            if (ConsultaDao.salvarConsulta(consulta)) {
                                promovido = true;
                                continue;
                            }
                        }
                    } catch (DateTimeParseException e) {
                        linhasAtualizadas.add(linha);
                    }
                } else {
                    linhasAtualizadas.add(linha);
                }
            }

            if (promovido) {
                Files.write(path, linhasAtualizadas);
            }
        } catch (IOException e) {
            mostrarAlerta("Erro", "Falha ao processar lista de espera: " + e.getMessage());
        }
    }

    @FXML
    public void handleVoltar() {
        Stage atual = (Stage) buttonVoltar.getScene().getWindow();
        Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente",
                controller -> ((PacienteController) controller).setPaciente(paciente));
    }
}