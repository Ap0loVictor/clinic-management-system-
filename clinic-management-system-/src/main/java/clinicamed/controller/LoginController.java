package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import clinicamed.utils.Navegacao;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final String PACIENTE_PROMPT = "Paciente";
    private static final String MEDICO_PROMPT = "Médico";
    @FXML
    private TextField campoNome;
    @FXML
    private PasswordField campoSenha;
    @FXML
    private RadioButton radioMedico;
    @FXML
    private RadioButton radioPaciente;
    @FXML
    private ToggleGroup grupoTipo;
    @FXML
    private Button botaoEntrar;
    @FXML
    private Label labelMensagem;
    @FXML
    private Hyperlink linkCadastro;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ainda sera implementada
    }
    public void handleEntrar() {
        if(!validarCampos()){
            labelMensagem.setText("Preencha todos os campos.");
            return;
        }
        String nome = campoNome.getText().trim();
        String senha = campoSenha.getText().trim();
        String tipoUsuario = ((RadioButton) grupoTipo.getSelectedToggle()).getText();
        if(tipoUsuario.equals(PACIENTE_PROMPT)) {
            Paciente paciente = verificarPaciente(nome, senha);
            if(paciente != null) {
                abrirTelaPaciente(paciente);
            } else {
                labelMensagem.setText("Paciente não encontrado");
            }
        } else if(tipoUsuario.equals(MEDICO_PROMPT)) {
            Medico medico = verificarMedico(nome, senha);
            if(medico != null) {
                abrirTelaMedico(medico);
            } else {
                labelMensagem.setText("Médico não encontrado");
            }
        }
    }
    public boolean validarCampos() {
        return !campoNome.getText().trim().isEmpty() && !campoSenha.getText().trim().isEmpty() && grupoTipo.getSelectedToggle() != null;
    }
    public Paciente verificarPaciente(String nome, String senha) {
        ArrayList<Paciente> pacientes = PacienteDao.carregarPacientes();
        for (Paciente paciente : pacientes) {
            if(paciente.getNome().equals(nome) && paciente.getSenha().equals(senha)) {
                return paciente;
            }
        }
        return null;
    }
    public Medico verificarMedico(String nome, String senha) {
        ArrayList<Medico> medicos = MedicoDao.carregarMedico();
        for (Medico medico : medicos) {
            if(medico.getNome().equals(nome) && medico.getSenha().equals(senha)) {
                return medico;
            }
        }
        return null;
    }
    public void abrirTelaPaciente(Paciente paciente) {
        Stage stageAtual = (Stage) botaoEntrar.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/TelaPaciente.fxml", "Área do Paciente",
            controller -> {
                if (controller instanceof PacienteController) {((PacienteController) controller).setPaciente(paciente);}
            });
}
    public void abrirTelaMedico(Medico medico) {
        Stage stageAtual = (Stage) botaoEntrar.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/TelaMedico.fxml", "Área do Médico",
            controller -> {
                if (controller instanceof MedicoController) {((MedicoController) controller).setMedico(medico);}
            });
    }
    public void handleCadastrar() {
        Stage stageAtual = (Stage) botaoEntrar.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/Cadastro.fxml", "Cadastro de Usuário");

    }
}
