package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.dao.PacienteDao;
import clinicamed.model.Consulta;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class MedicoController extends Basecontroller implements Initializable {
    private Medico medico;

    @FXML private Label labelNome;
    @FXML private Label labelNomeTitle;
    @FXML private Label labelEspecialidade;
    @FXML private Label labelPlanoSaude;
    @FXML private Button buttonAtendido;
    @FXML private Button buttonSair;
    @FXML private Button editarPerfil;
    @FXML private TableView<Consulta> tableConsultasMarcadas;
    @FXML private TableColumn<Consulta, String> columnPaciente;
    @FXML private TableColumn<Consulta, String> columData;
    @FXML private TableColumn<Consulta, String> columnStatus;
    @FXML private TableColumn<Consulta, String> columnHorario;
    @FXML private TableColumn<Consulta, String> columnVerDescricao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColunasTabela();
    }

    private void configurarColunasTabela() {
        columnPaciente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomePaciente()));
        columData.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getData().toString()));
        columnHorario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHorario()));
        columnStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));
        columnVerDescricao.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescricao()));
    }

    private void carregarConsultasMedico() {
        List<Consulta> todasConsultas = ConsultaDao.carregarConsultas();
        ObservableList<Consulta> consultasDoMedico = FXCollections.observableArrayList();
   
        for (Consulta c : todasConsultas) {
            if (c.getNomeMedico().equalsIgnoreCase(medico.getNome())) {
                consultasDoMedico.add(c);
            }
        }

        tableConsultasMarcadas.setItems(consultasDoMedico);
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        labelNome.setText(medico.getNome());
        labelNomeTitle.setText(medico.getNome());
        labelEspecialidade.setText(medico.getEspecialidade());
        labelPlanoSaude.setText(medico.getPlanoSaude());

        mostrarDados();
        carregarConsultasMedico();
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

    public void handleSair() {
        Stage stageAtual = (Stage) buttonSair.getScene().getWindow();
        Navegacao.voltarParaLogin(stageAtual);
    }

    public void handleEditarPerfil() {
        Stage stageAtual = (Stage) editarPerfil.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/EditarPerfil.fxml", "Editar Perfil", controller -> {
            ((EditarController) controller).setUsuario(medico);
        });
    }

    public void mostrarEspecialidade() {
        labelEspecialidade.setText(medico.getEspecialidade());
    }

    public void mostrarPlanoSaude() {
        labelPlanoSaude.setText(medico.getPlanoSaude());
    }

    @Override
    public Button getBotaoSair() {
        return buttonSair;
    }

    @FXML
    private void handleAtenderConsulta() {
    Consulta consultaSelecionada = tableConsultasMarcadas.getSelectionModel().getSelectedItem();
    if (consultaSelecionada == null) {
        mostrarAlerta("Selecione uma consulta para atender.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AtenderConsulta.fxml"));
        Parent root = loader.load();

        AtenderConsultaController controller = loader.getController();
        Paciente paciente = PacienteDao.buscarPorNome(consultaSelecionada.getNomePaciente());
        controller.setConsulta(consultaSelecionada, paciente);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Atender Consulta");

        // Quando a janela de atendimento for fechada, recarrega a tabela
        stage.setOnHidden(e -> carregarConsultasMedico());

        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
} 