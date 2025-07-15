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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CadastrarConsultaController implements Initializable {

    @FXML private ComboBox<String> comboEspecialidades;
    @FXML private ComboBox<String> comboMedicos;
    @FXML private TextField campoPaciente;
    @FXML private TextField campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private Button buttonSalvar;
    @FXML private Button buttonVoltar;
    @FXML private ComboBox<String> comboPlano;

    private Paciente paciente;
    private List<Medico> medicosFiltrados;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;

        campoPaciente.setText(paciente.getNome());
        campoPaciente.setEditable(false);
        comboPlano.getItems().add("Não tenho");
        comboPlano.getItems().add(paciente.getPlanoSaude());

        if (paciente.temPlano()) {
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
            // Adiciona a especialidade apenas se ainda não estiver na lista
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
        if (especialidadeSelecionada == null || planoSelecionado == null ) return;

        comboMedicos.getItems().clear();

        boolean temPlano = !planoSelecionado.equalsIgnoreCase("Não tenho");

        if (!temPlano) {
        // Paciente sem plano: busca todos os médicos da especialidade selecionada, sem filtrar por plano
            medicosFiltrados = MedicoDao.carregarMedicos().stream().filter(m -> m.getEspecialidade().equalsIgnoreCase(especialidadeSelecionada)).collect(Collectors.toList());
        } else {
        // Paciente sem plano: busca todos os médicos da especialidade selecionada, sem filtrar por plano
            medicosFiltrados = MedicoDao.carregarMedicos().stream().filter(m -> m.getEspecialidade().equalsIgnoreCase(especialidadeSelecionada)).filter(m -> m.getPlanoSaude().equalsIgnoreCase(planoSelecionado)).collect(Collectors.toList());
        }

        for (Medico m : medicosFiltrados) {
            comboMedicos.getItems().add(m.getNome());
        }
    }


    @FXML
    private void handleSalvarConsulta() {
        String nomeMedico = comboMedicos.getValue();
        String data = campoData.getText();
        String horario = campoHorario.getText();
        String descricao = campoDescricao.getText();

        if (nomeMedico == null || data.isEmpty() || horario.isEmpty()) {
            mostrarAlerta("Preencha todos os campos obrigatórios!");
            return;
        }

        Medico medicoSelecionado = medicosFiltrados.stream()
            .filter(m -> m.getNome().equals(nomeMedico))
            .findFirst()
            .orElse(null);

        if (medicoSelecionado == null || ConsultaDao.contarConsultasPorData(medicoSelecionado, data) >= 3) {
            adicionarListaEspera(paciente.getNome(), nomeMedico, data, horario, "Em espera", descricao);
            mostrarAlerta("Você foi adicionado à lista de espera pois o médico já está com a agenda cheia nesta data.");
            return;
        }

        Consulta consulta = new Consulta(medicoSelecionado.getNome(), paciente.getNome(), data, horario, "Marcada", descricao);
        ConsultaDao.salvarConsulta(consulta);

        mostrarAlerta("Consulta marcada com sucesso!");
        handleVoltar();
    }
    public void adicionarListaEspera(String paciente, String medico, String data, String horario, String status, String descricao) {
        String linha = medico+ "\t" + paciente + "\t" + data + "\t" + horario + "\t" + status + "\t" + descricao;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lista_espera.txt", true))) {
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void handleVoltar() {
        Stage atual = (Stage) buttonVoltar.getScene().getWindow();
        Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
            ((PacienteController) controller).setPaciente(paciente);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialização se necessário
    }
}