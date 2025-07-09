package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
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
        boolean loginValido = tipoUsuario.equals(MEDICO_PROMPT) && verificarMedico(nome, senha) || tipoUsuario.equals(PACIENTE_PROMPT) && verificarPaciente(nome, senha);
        if(loginValido) {
            labelMensagem.setText("Login bem-sucedido!");
            abrirTelaUsuario(tipoUsuario);
        } else {
            labelMensagem.setText("Usuário ou senha inválidos.");
        }
    }
    public boolean validarCampos() {
        return !campoNome.getText().trim().isEmpty() && !campoSenha.getText().trim().isEmpty() && grupoTipo.getSelectedToggle() != null;
    }
    public boolean verificarPaciente(String nome, String senha) {
        ArrayList<Paciente> pacientes = PacienteDao.carregarPacientes();
        for (Paciente paciente : pacientes) {
            if(paciente.getNome().equals(nome) && paciente.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }
    public boolean verificarMedico(String nome, String senha) {
        ArrayList<Medico> medicos = MedicoDao.carregarMedico();
        for (Medico medico : medicos) {
            if(medico.getNome().equals(nome) && medico.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }
    public void abrirTelaUsuario(String tipoUsuario) {
        String caminho = "";
        if(tipoUsuario.equals(PACIENTE_PROMPT)) {
            caminho = "/view/TelaPaciente.fxml";
        } else if(tipoUsuario.equals(MEDICO_PROMPT)) {
            caminho = "/view/TelaMedico.fxml";
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Área do " + tipoUsuario);
            stage.setScene(new Scene(root));
            stage.show();

            // Fecha a janela atual (login)
            Stage atual = (Stage) campoNome.getScene().getWindow();
            atual.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Erro ao carregar a tela de " + tipoUsuario);
            alerta.setContentText("Tente novamente.");
            alerta.show();
        }
    }
    public void handleCadastrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Cadastro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Usuário");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Aqui você pode exibir uma mensagem de erro para o usuário se quiser
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Erro ao carregar tela de cadastro.");
            alerta.setContentText("Tente novamente.");
            alerta.show();
        }
    }
}
