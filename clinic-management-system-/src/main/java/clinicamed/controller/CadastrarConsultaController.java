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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CadastrarConsultaController implements Initializable {

    @FXML private ComboBox<String> comboEspecialidades;
    @FXML private ComboBox<String> comboMedicos;
    @FXML private TextField campoPaciente;
    @FXML private TextField campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private Button buttonSalvar;
    @FXML private Button buttonVoltar;

    private Paciente paciente;
    private List<Medico> medicosFiltrados;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;

        campoPaciente.setText(paciente.getNome());
        campoPaciente.setEditable(false);
        carregarEspecialidades();
    }

    private void carregarEspecialidades() {
        comboEspecialidades.getItems().addAll(MedicoDao.listarEspecialidades());
    }

    @FXML
    private void atualizarMedicos() {
        String especialidadeSelecionada = comboEspecialidades.getValue();
        if (especialidadeSelecionada == null) return;

        comboMedicos.getItems().clear();
        medicosFiltrados = MedicoDao.buscarPorEspecialidadeEPlano(especialidadeSelecionada, paciente.temPlano());
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

        // if (medicoSelecionado == null || ConsultaDao.contarConsultasPorData(medicoSelecionado, data) >= 3) {
        //     mostrarAlerta("Este médico já está com a agenda cheia nesta data.");
        //     return;
        // }

        Consulta consulta = new Consulta(medicoSelecionado.getNome(), paciente.getNome(), data, horario, "Marcada", descricao);
        ConsultaDao.salvarConsulta(consulta);

        mostrarAlerta("Consulta marcada com sucesso!");
        handleVoltar();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void handleVoltar() {
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