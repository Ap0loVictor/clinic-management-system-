package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastrarConsultaController extends Basecontroller implements Initializable {
    private Paciente paciente;
    @FXML private TextField campoMedico;
    @FXML private TextField campoPaciente;
    @FXML private TextField campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private Button buttonSair;
    @FXML private Button salvarConsulta;
    @FXML private Button voltar;

    private String nomePaciente;

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        nomePaciente = paciente.getNome();
        campoPaciente.setText(nomePaciente);
        campoPaciente.setEditable(false); 
    }
    public void handleVoltar() {
        Stage atual = (Stage) voltar.getScene().getWindow();
        Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
            ((PacienteController) controller).setPaciente(paciente);
        });
    }
    @FXML
    private void handleSalvarConsulta() {
        String medico = campoMedico.getText();
        String paciente = campoPaciente.getText(); // já está setado
        String data = campoData.getText();
        String horario = campoHorario.getText();
        String descricao = campoDescricao.getText();

        // Como o status será manipulado por médico depois, colocamos como padrão "Marcada"
        String status = "Marcada";

        Consulta consulta = new Consulta(medico, paciente, data, horario, status, descricao);
        ConsultaDao.salvarConsulta(consulta);
        handleVoltar();
    }

    @Override
    protected Button getBotaoSair() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
