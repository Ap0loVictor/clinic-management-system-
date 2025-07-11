package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.userfactory.MedicoFactory;
import clinicamed.userfactory.PacienteFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
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
            Paciente paciente = criarPaciente(nome, senha);
            labelCadastrado.setText("Paciente cadastrado com sucesso!");
            loginController.abrirTelaPaciente(paciente);
        } else if (radioMedico.isSelected()) {
            Medico medico = criarMedico(nome, senha);
            labelCadastrado.setText("Médico cadastrado com sucesso!");
            loginController.abrirTelaMedico(medico);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPaciente.fxml"));
            Parent root = loader.load();

            PacienteController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage stage = new Stage();
            stage.setTitle("Área do Paciente");
            stage.setScene(new Scene(root));
            stage.show();

            fecharJanelaAtual();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir tela do paciente", "Tente novamente.");
        }
    }

    public void abrirTelaMedico(Medico medico) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaMedico.fxml"));
            Parent root = loader.load();

            MedicoController controller = loader.getController();
            controller.setMedico(medico);

            Stage stage = new Stage();
            stage.setTitle("Área do Médico");
            stage.setScene(new Scene(root));
            stage.show();

            fecharJanelaAtual();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir tela do médico", "Tente novamente.");
        }
    }

    private void fecharJanelaAtual() {
        Stage atual = (Stage) nomeUser.getScene().getWindow();
        atual.close();
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

}
