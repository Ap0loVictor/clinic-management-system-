package clinicamed.controller;

import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PacienteController implements Initializable {
    private Paciente paciente;

    @FXML
    private Label labelNome;
    @FXML
    private Label labelNomeTitle;
    @FXML
    private Label labelIdade;
    @FXML
    private Label labelPlanoSaude;
    @FXML
    private Button buttonEditarPerfil;
    @FXML
    private Button buttonMinhasConsultas;
    @FXML
    private Button buttonMarcarConsulta;
    @FXML
    private Button buttonMedicosDisponiveis;
    @FXML
    private Button buttonSair;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialização, se necessário
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        mostrarDados();
    }

    private void mostrarDados() {
        mostrarNomes();
        mostrarIdade();
        mostrarPlanoSaude();
    }

    private void mostrarNomes() {
        labelNomeTitle.setText(paciente.getNome());
        labelNome.setText(paciente.getNome());
    }

    private void mostrarIdade() {
        labelIdade.setText(String.valueOf(paciente.getIdade()));
    }

    private void mostrarPlanoSaude() {
        labelPlanoSaude.setText(paciente.isTemPlano() ? "Sim" : "Não");
    }

    @FXML
    private void handleEditarPerfil() {
        Stage stageAtual = (Stage) buttonEditarPerfil.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/EditarPerfil.fxml", "Editar Perfil", controller -> {
            ((EditarController) controller).setUsuario(paciente);
        });
    }

    @FXML
private void handleMarcarConsulta() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaCadastrarConsulta.fxml"));
        Parent root = loader.load();
        clinicamed.controller.CadastrarConsultaController cadastroController = loader.getController();
        cadastroController.setPaciente(paciente.getNome());
        Stage stage = new Stage();
        stage.setTitle("Marcar Consulta");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void handleSair() {
        Stage stageAtual = (Stage) buttonSair.getScene().getWindow();
        Navegacao.voltarParaLogin(stageAtual);
    }
}
