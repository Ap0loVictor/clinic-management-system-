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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPaciente.fxml"));
            Parent root = loader.load();

            PacienteController controller = loader.getController();
            controller.setPaciente(paciente);
            Stage stage = new Stage();
            stage.setTitle("Área do Paciente");
            stage.setScene(new Scene(root));
            stage.show();
            Stage atual = (Stage) botaoEntrar.getScene().getWindow();
            atual.close();
        } catch (IOException e) {
            mostrarAlertaErro("Erro ao abrir tela", "Ocorreu um erro ao abrir a área do Paciente");
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
            Stage atual = (Stage) botaoEntrar.getScene().getWindow();
            atual.close();

        } catch (IOException e) {
            mostrarAlertaErro("Erro ao abrir tela", "Ocorreu um erro ao abrir a área do Médico");
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
            Stage atual = (Stage) linkCadastro.getScene().getWindow();
            atual.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Aqui você pode exibir uma mensagem de erro para o usuário se quiser
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Erro ao carregar tela de cadastro.");
            alerta.setContentText("Tente novamente.");
            alerta.show();
        }
    }
    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
