package clinicamed.controller;

import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.model.Usuario;

import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarController extends Basecontroller implements Initializable {
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
        Stage atual = (Stage) buttonVoltar.getScene().getWindow();
        if (usuario instanceof Paciente paciente) {
            Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
                ((PacienteController) controller).setPaciente(paciente);
            });
        } else if (usuario instanceof Medico medico) {
            Navegacao.trocarTela(atual, "/view/TelaMedico.fxml", "Área do Médico", controller -> {
                ((MedicoController) controller).setMedico(medico);
            });
        }
    }
    @Override
    protected Button getBotaoSair() {
        return buttonVoltar;
    }
}
