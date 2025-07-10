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

import java.net.URL;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {
    LoginController loginController = new LoginController();
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
            criarPaciente(nome, senha);
            labelCadastrado.setText("Paciente cadastrado com sucesso!");
            loginController.abrirTelaUsuario("Paciente");
        } else if (radioMedico.isSelected()) {
            criarMedico(nome, senha);
            labelCadastrado.setText("Médico cadastrado com sucesso!");
            loginController.abrirTelaUsuario("Médico");
        } else {
            labelCadastrado.setText("Selecione um tipo de usuário para cadastrar");
        }
    }
    public void criarPaciente(String nome, String senha) {
        int idade = Integer.parseInt(idadeUser.getText());
        boolean temPlano = pacienteTemPlano.isSelected();
        Paciente paciente = PacienteFactory.criarFromController(nome, senha, idade, temPlano);
        PacienteDao.salvarPaciente(paciente);
    }
    public void criarMedico(String nome, String senha) {
            String especialidade = especialidadeMedico.getText();
            String planoSaude = planoSaudeMedico.getText();
            Medico medico = MedicoFactory.criarFromController(nome, senha, especialidade, planoSaude);
            MedicoDao.salvarMedico(medico);
    }
}
