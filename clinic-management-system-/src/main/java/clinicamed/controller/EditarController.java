package clinicamed.controller;

import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditarController implements Initializable {
    private Usuario usuario;
    @FXML
    private Label labelIdade;
    @FXML
    private Label labelPlanoMedico;
    @FXML
    private Label labelPlano;
    @FXML
    private Label labelEspecialidade;
    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoSenha;
    @FXML
    private TextField campoIdade;
    @FXML
    private TextField campoEspecialidade;
    @FXML
    private TextField campoPlanoSaude;
    @FXML
    private RadioButton radioTemPlanoSaude;
    @FXML
    private RadioButton radioNaoTemPlano;
    @FXML
    private Button buttonSalvar;
    @FXML
    private Button buttonVoltar;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if(usuario instanceof Medico medico){
            mostrarDadosMedico(medico);
        } else if(usuario instanceof Paciente paciente) {
            mostrarDadosPaciente(paciente);
        }
    }
    public void mostrarDadosMedico(Medico medico) {
        campoNome.setText(medico.getNome());
        campoSenha.setText(medico.getSenha());
        campoEspecialidade.setText(medico.getEspecialdiade());
        campoPlanoSaude.setText(medico.getPlanoSaude());
        campoEspecialidade.setVisible(true);
        campoPlanoSaude.setVisible(true);
        campoIdade.setVisible(false);
        radioTemPlanoSaude.setVisible(false);
        radioNaoTemPlano.setVisible(false);
        labelIdade.setVisible(false);
        labelPlano.setVisible(false);

    }
    public void mostrarDadosPaciente(Paciente paciente) {
        campoNome.setText(paciente.getNome());
        campoSenha.setText(paciente.getSenha());
        campoIdade.setText(String.valueOf(paciente.getIdade()));
        radioTemPlanoSaude.setSelected(paciente.isTemPlano());
        radioNaoTemPlano.setSelected(!paciente.isTemPlano());
        campoIdade.setVisible(true);
        radioTemPlanoSaude.setVisible(true);
        radioNaoTemPlano.setVisible(true);
        campoEspecialidade.setVisible(false);
        campoPlanoSaude.setVisible(false);
        labelEspecialidade.setVisible(false);
        labelPlanoMedico.setVisible(false);

    }
    public void handleVoltar() {
        try{
            FXMLLoader loader;
            if(usuario instanceof Paciente paciente) {
              loader = new FXMLLoader(getClass().getResource("/view/TelaPaciente.fxml"));
              Parent root = loader.load();
              PacienteController pacienteController = loader.getController();
              pacienteController.setPaciente(paciente);
            } else if(usuario instanceof Medico medico) {
                loader = new FXMLLoader(getClass().getResource("/view/TelaMedico.fxml"));
                Parent root = loader.load();
                MedicoController medicoController = loader.getController();
                medicoController.setMedico(medico);
            } else {
                return;
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Tela Principal");
            stage.setScene(new Scene(root));
            stage.show();
            Stage atual = (Stage) buttonVoltar.getScene().getWindow();
            atual.close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Não foi possível voltar");
            alerta.setContentText("Erro ao carregar a tela anterior.");
            alerta.show();
        }
    }
}
