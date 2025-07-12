package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CadastrarConsultaController {

    @FXML private TextField campoMedico;
    @FXML private TextField campoPaciente;
    @FXML private TextField campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private ComboBox<String> comboStatus;

    @FXML
    public void initialize() {
        comboStatus.getItems().addAll("Marcada", "Confirmada", "Concluída");
    }

    @FXML
    private void handleSalvarConsulta() {
        String medico = campoMedico.getText();
        String paciente = campoPaciente.getText();
        String data = campoData.getText();
        String horario = campoHorario.getText();
        String status = comboStatus.getValue();
        String descricao = campoDescricao.getText();

        Consulta consulta = new Consulta(medico, paciente, data, horario, status, descricao);
        ConsultaDao.salvarConsulta(consulta);

        System.out.println("Consulta salva com sucesso!");
    }
}
