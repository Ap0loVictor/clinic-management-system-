package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.userfactory.MedicoFactory;
import clinicamed.userfactory.PacienteFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import clinicamed.utils.Navegacao;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastroController extends Basecontroller implements Initializable {
    @FXML
    private RadioButton radioPaciente;
    @FXML
    private TextField idadeUser;
    @FXML
    private RadioButton pacienteTemPlano;
    @FXML
    private RadioButton pacienteNaotemPlano;
    @FXML
    private RadioButton radioMedico;
    @FXML
    private TextField planoSaudeMedico;
    @FXML
    private TextField especialidadeMedico;
    @FXML
    private AnchorPane campoPaciente;
    @FXML
    private AnchorPane campoMedico;
    @FXML
    private TextField nomeUser;
    @FXML
    private PasswordField senhaUser;
    @FXML
    private Label labelCadastrado;
    @FXML
    private Button buttonCadastrar;
    @FXML
    private Button buttonSair;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ainda sera implementado
    }
    public void atualizarCampos() {
        boolean isPaciente = radioPaciente.isSelected();
        campoPaciente.setVisible(isPaciente);
        campoPaciente.setManaged(isPaciente);
        campoMedico.setVisible(!isPaciente);
        campoMedico.setManaged(!isPaciente);
    }
    public void handleCadastrar() {
        String nome = nomeUser.getText();
        String senha = senhaUser.getText();
        if(radioPaciente.isSelected()) {
            Paciente paciente = criarPaciente(nome, senha);
            labelCadastrado.setText("Paciente cadastrado com sucesso!");
            abrirTelaPaciente(paciente);
        } else if (radioMedico.isSelected()) {
            Medico medico = criarMedico(nome, senha);
            labelCadastrado.setText("Médico cadastrado com sucesso!");
            abrirTelaMedico(medico);
        } else {
            labelCadastrado.setText("Preencha todos os campos corretamente.");
        }
    }
    public Paciente criarPaciente(String nome, String senha) {
        int idade = Integer.parseInt(idadeUser.getText());
        boolean temPlano = pacienteTemPlano.isSelected();
        Paciente paciente = PacienteFactory.criarFromController(nome, senha, idade, temPlano);
        PacienteDao.salvarPaciente(paciente);
        return paciente;
    }
    public Medico criarMedico(String nome, String senha) {
            String especialidade = especialidadeMedico.getText();
            String planoSaude = planoSaudeMedico.getText();
            Medico medico = MedicoFactory.criarFromController(nome, senha, especialidade, planoSaude);
            MedicoDao.salvarMedico(medico);
            return medico;
    }
    public void abrirTelaPaciente(Paciente paciente) {
        Stage stageAtual = (Stage) buttonCadastrar.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/TelaPaciente.fxml", "Área do Paciente",
            controller -> {
                if (controller instanceof PacienteController) {((PacienteController) controller).setPaciente(paciente);}
            });
}
    public void abrirTelaMedico(Medico medico) {
        Stage stageAtual = (Stage) buttonCadastrar.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/TelaMedico.fxml", "Área do Médico",
            controller -> {
                if (controller instanceof MedicoController) {((MedicoController) controller).setMedico(medico);}
            });
    }

    @Override
    public Button getBotaoSair() {
        return buttonSair;
    }

}
