package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CadastrarConsultaController extends Basecontroller {

    @FXML private TextField campoMedico;
    @FXML private TextField campoPaciente;
    @FXML private TextField campoData;
    @FXML private TextField campoHorario;
    @FXML private TextArea campoDescricao;
    @FXML private Button buttonSair;

    private String nomePaciente;

    public void setPaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
        campoPaciente.setText(nomePaciente);
        campoPaciente.setEditable(false); 
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

        System.out.println("Consulta salva com sucesso!");
    }
    @Override
    public Button getBotaoSair() {
        return buttonSair;
    }
}
