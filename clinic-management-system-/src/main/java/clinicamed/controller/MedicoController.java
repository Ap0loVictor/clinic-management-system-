package clinicamed.controller;

import clinicamed.model.Consulta;
import clinicamed.model.Medico;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicoController extends Basecontroller implements Initializable {
    private Medico medico;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelNomeTitle;
    @FXML
    private Label labelEspecialidade;
    @FXML
    private Label labelPlanoSaude;
    @FXML
    private Button buttonAtendido;
    @FXML
    private Button buttonSair;
    @FXML
    private Button buttonEditarPerfil;
    @FXML
    private TableView tableConsultasMarcadas;
    @FXML
    private TableColumn<Consulta, String> columnPaciente;
    @FXML
    private TableColumn<Consulta, String> columData;
    @FXML
    private TableColumn<Consulta, String> columnStatus;
    @FXML
    private TableColumn<Consulta, String> columnHorario;
    @FXML
    private TableColumn<Consulta, String> columnVerDescricao;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnPaciente.setCellValueFactory(new PropertyValueFactory<>("nomePaciente"));
        columData.setCellValueFactory(new PropertyValueFactory<>("data"));
        columnHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnVerDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }
  
    public void setMedico(Medico medico) {
        this.medico = medico;
        mostrarDados();
    }
    public void mostrarDados() {
        mostrarNomes();
        mostrarEspecialidade();
        mostrarPlanoSaude();
    }
    public void mostrarNomes() {
        labelNomeTitle.setText(medico.getNome());
        labelNome.setText(medico.getNome());
    }
    
    public void handleEditarPerfil() {

        Stage stageAtual = (Stage) getBotaoSair().getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/EditarPerfil.fxml", "Editar Perfil");

    }
    public void mostrarEspecialidade() {
        labelEspecialidade.setText(medico.getEspecialdiade());
    }
    public void mostrarPlanoSaude() {
        labelPlanoSaude.setText(medico.getPlanoSaude());
    }
    @Override
    public Button getBotaoSair() {
        return buttonSair;
    }

}
