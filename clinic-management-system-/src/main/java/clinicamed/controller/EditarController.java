package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.model.Usuario;
import clinicamed.utils.Navegacao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarController extends Basecontroller implements Initializable {
    private Usuario usuario;
    
    @FXML private Label labelIdade;
    @FXML private Label labelPlanoMedico;
    @FXML private Label labelPlano;
    @FXML private Label labelEspecialidade;
    @FXML private Label labelQualPlano;
    
    @FXML private TextField campoNome;
    @FXML private PasswordField campoSenha;
    @FXML private TextField campoIdade;
    @FXML private TextField campoEspecialidade;
    @FXML private TextField campoPlanoSaude;
    
    @FXML private ComboBox<String> comboPlanos; // Novo ComboBox
    
    @FXML private RadioButton radioTemPlanoSaude;
    @FXML private RadioButton radioNaoTemPlano;
    
    @FXML private Button buttonSalvar;
    @FXML private Button buttonVoltar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carregar planos disponíveis no ComboBox
        comboPlanos.setItems(FXCollections.observableArrayList(PacienteDao.getPlanosDisponiveis()));
        
        // Listener para mostrar/ocultar campo do plano
        radioTemPlanoSaude.selectedProperty().addListener((obs, oldVal, newVal) -> {
            comboPlanos.setVisible(newVal);
            labelQualPlano.setVisible(newVal);
            
            if(newVal) {
                comboPlanos.requestFocus();
            }
        });
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
        campoEspecialidade.setText(medico.getEspecialidade());
        campoPlanoSaude.setText(medico.getPlanoSaude());
        
        // Esconder campos do paciente
        campoIdade.setVisible(false);
        radioTemPlanoSaude.setVisible(false);
        radioNaoTemPlano.setVisible(false);
        labelIdade.setVisible(false);
        labelPlano.setVisible(false);
        comboPlanos.setVisible(false);
        labelQualPlano.setVisible(false);
        
        // Mostrar campos do médico
        campoEspecialidade.setVisible(true);
        campoPlanoSaude.setVisible(true);
        labelEspecialidade.setVisible(true);
        labelPlanoMedico.setVisible(true);
    }
    
    public void mostrarDadosPaciente(Paciente paciente) {
        campoNome.setText(paciente.getNome());
        campoSenha.setText(paciente.getSenha());
        campoIdade.setText(String.valueOf(paciente.getIdade()));
        
        // Configurar plano
        if (paciente.getPlanoSaude().equals("Não tenho")) {
            radioNaoTemPlano.setSelected(true);
            comboPlanos.setVisible(false);
            labelQualPlano.setVisible(false);
        } else {
            radioTemPlanoSaude.setSelected(true);
            comboPlanos.setValue(paciente.getPlanoSaude());
            comboPlanos.setVisible(true);
            labelQualPlano.setVisible(true);
        }
        
        // Esconder campos do médico
        campoEspecialidade.setVisible(false);
        campoPlanoSaude.setVisible(false);
        labelEspecialidade.setVisible(false);
        labelPlanoMedico.setVisible(false);
        
        // Mostrar campos do paciente
        campoIdade.setVisible(true);
        radioTemPlanoSaude.setVisible(true);
        radioNaoTemPlano.setVisible(true);
        labelIdade.setVisible(true);
        labelPlano.setVisible(true);
    }
    
    public void handleSalvar() {
        if (usuario instanceof Medico medico) {
            salvarMedico(medico);
        } else if (usuario instanceof Paciente paciente) {
            salvarPaciente(paciente);
        }
    }
    
    private void salvarMedico(Medico medico) {
        // Validar campos
        if (campoNome.getText().isEmpty() || campoSenha.getText().isEmpty() ||
            campoEspecialidade.getText().isEmpty() || campoPlanoSaude.getText().isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos.");
            return;
        }
        
        medico.setNome(campoNome.getText());
        medico.setSenha(campoSenha.getText());
        medico.setEspecialidade(campoEspecialidade.getText());
        medico.setPlanoSaude(campoPlanoSaude.getText());
        
        // Atualizar no DAO
        MedicoDao.atualizarMedico(medico);
        
        mostrarAlerta("Sucesso", "Dados do médico atualizados!");
    }
    
    private void salvarPaciente(Paciente paciente) {
        // Validar campos
        if (campoNome.getText().isEmpty() || campoSenha.getText().isEmpty() || 
            campoIdade.getText().isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos.");
            return;
        }
        
        // Validar idade
        int idade;
        try {
            idade = Integer.parseInt(campoIdade.getText());
            if (idade < 0 || idade > 120) {
                mostrarAlerta("Idade inválida", "A idade deve ser entre 0 e 120 anos.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Idade inválida", "Digite um número válido para idade.");
            return;
        }
        
        paciente.setNome(campoNome.getText());
        paciente.setSenha(campoSenha.getText());
        paciente.setIdade(idade);
        
        // Atualizar plano
        if (radioTemPlanoSaude.isSelected()) {
            if (comboPlanos.getValue() == null) {
                mostrarAlerta("Plano inválido", "Selecione um plano de saúde.");
                return;
            }
            paciente.setPlanoSaude(comboPlanos.getValue());
        } else {
            paciente.setPlanoSaude("Não tenho");
        }
        
        // Atualizar no DAO
        PacienteDao.atualizarPaciente(paciente);
        
        mostrarAlerta("Sucesso", "Dados do paciente atualizados!");
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
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @Override
    protected Button getBotaoSair() {
        return buttonVoltar;
    }
}