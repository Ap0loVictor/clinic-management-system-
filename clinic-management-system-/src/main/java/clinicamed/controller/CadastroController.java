package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.userfactory.MedicoFactory;
import clinicamed.userfactory.PacienteFactory;
import javafx.collections.FXCollections;
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
    @FXML private ComboBox<String> comboPlanos; // Novo ComboBox
    @FXML private Label labelQualPlano;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carregar planos disponíveis no ComboBox
        comboPlanos.setItems(FXCollections.observableArrayList(PacienteDao.getPlanosDisponiveis()));
        
        // Listener para mostrar/ocultar campo do plano
        pacienteTemPlano.selectedProperty().addListener((obs, oldVal, newVal) -> {
            comboPlanos.setVisible(newVal);
            labelQualPlano.setVisible(newVal);
            
            if(newVal) {
                comboPlanos.requestFocus();
            }
        });
    }
    public void atualizarCampos() {
        boolean isPaciente = radioPaciente.isSelected();
        campoPaciente.setVisible(isPaciente);
        campoPaciente.setManaged(isPaciente);
        campoMedico.setVisible(!isPaciente);
        campoMedico.setManaged(!isPaciente);
    }
       public void handleCadastrar() {
        String nome = nomeUser.getText().trim();
        String senha = senhaUser.getText().trim();
        
        // Validação básica de campos
        if(nome.isEmpty() || senha.isEmpty()) {
            labelCadastrado.setText("Preencha nome e senha!");
            return;
        }
        
        if(radioPaciente.isSelected()) {
            Paciente paciente = criarPaciente(nome, senha);
            if(paciente != null) {
                labelCadastrado.setText("Paciente cadastrado com sucesso!");
                abrirTelaPaciente(paciente);
            }
        } else if (radioMedico.isSelected()) {
            Medico medico = criarMedico(nome, senha);
            if(medico != null) {
                labelCadastrado.setText("Médico cadastrado com sucesso!");
                abrirTelaMedico(medico);
            }
        } else {
            labelCadastrado.setText("Selecione o tipo de usuário.");
        }
    }
    public Paciente criarPaciente(String nome, String senha) {
        // Validar idade
        int idade;
        try {
            idade = Integer.parseInt(idadeUser.getText());
            if(idade < 0 || idade > 120) {
                mostrarAlerta("Idade inválida", "A idade deve ser entre 0 e 120 anos");
                return null;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Idade inválida", "Digite um número válido para idade");
            return null;
        }
        
        String plano;
        if (pacienteTemPlano.isSelected()) {
            plano = comboPlanos.getValue();
            if(plano == null || plano.isEmpty()) {
                mostrarAlerta("Plano inválido", "Selecione um plano de saúde");
                return null;
            }
        } else {
            plano = "Não tenho";
        }
        
        Paciente paciente = PacienteFactory.criarFromController(nome, senha, idade, plano);
        PacienteDao.salvarPaciente(paciente);
        return paciente;
    }
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
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
