package clinicamed.controller;

import clinicamed.model.Consulta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicoController implements Initializable {
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
}
