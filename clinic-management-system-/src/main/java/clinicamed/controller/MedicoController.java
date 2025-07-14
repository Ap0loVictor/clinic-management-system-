package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Medico;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

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
    private Button editarPerfil;
    @FXML
    private TableView<Consulta> tableConsultasMarcadas;
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
    @FXML
    private Button buttonEditarPerfil;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColunasTabela();
    }

    private void configurarColunasTabela() {
    columnPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomePaciente()));
    columData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
    columnHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorario()));
    columnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    columnVerDescricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
    }

    private void carregarConsultasMedico() {
    ArrayList<Consulta> todasConsultas = ConsultaDao.carregarConsultas();
    ObservableList<Consulta> consultasDoMedico = FXCollections.observableArrayList();

    System.out.println("Médico logado: " + medico.getNome());   
    for (Consulta c : todasConsultas) {
        System.out.println("Consulta do médico: " + c.getNomeMedico());
        if (c.getNomeMedico().equalsIgnoreCase(medico.getNome())) {
            consultasDoMedico.add(c);
        }
    }

    tableConsultasMarcadas.setItems(consultasDoMedico);
    }

    @FXML
    private void handleEditarPerfil() {
        Stage stageAtual = (Stage) buttonEditarPerfil.getScene().getWindow();
        Navegacao.trocarTela(stageAtual, "/view/EditarPerfil.fxml", "Editar Perfil");
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        labelNome.setText(medico.getNome());
        labelNomeTitle.setText(medico.getNome());
        labelEspecialidade.setText(medico.getEspecialdiade());
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
