package clinicamed.controller;

import clinicamed.model.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PacienteController extends Basecontroller implements Initializable {
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

    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        mostrarDados();
    }
    public void mostrarDados() {
        mostrarNomes();
        mostrarIdade();
        mostrarPlanoSaude();
    }
    public void mostrarNomes() {
        labelNomeTitle.setText(paciente.getNome());
        labelNome.setText(paciente.getNome());
    }
    @FXML
    public void handleEditarPerfil() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditarPerfil.fxml"));
            Parent root = loader.load();
            EditarController controller = loader.getController();
            controller.setUsuario(paciente);
            Stage stage = new Stage();
            stage.setTitle("Editar Perfil");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void mostrarIdade() {
        labelIdade.setText(String.valueOf(paciente.getIdade()));
    }
    public void mostrarPlanoSaude() {
        labelPlanoSaude.setText(paciente.isTemPlano() ? "Sim" : "Não");
    }
    @Override
    public Button getBotaoSair() {
        return buttonSair;
    }
}
