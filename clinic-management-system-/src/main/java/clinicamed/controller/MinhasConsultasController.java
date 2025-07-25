package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MinhasConsultasController extends Basecontroller implements Initializable {

    private Paciente paciente;
    
    @FXML private Label labelNomePaciente;
    @FXML private Button voltar;
    @FXML private Button botaoAvaliar;
    @FXML private TableView<Consulta> consultas;
    @FXML private TableColumn<Consulta, String> nomeMedico;
    @FXML private TableColumn<Consulta, String> status;
    @FXML private TableColumn<Consulta, String> horario;
    @FXML private TableColumn<Consulta, String> data;

    @FXML private TableColumn<Consulta, String> avaliacao;


    @FXML private TableColumn<Consulta, String> descricao;
    @FXML private Button cancelarConsulta;

    @Override
    protected Button getBotaoSair() {
        return voltar;
    }

    @Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    nomeMedico.setCellValueFactory(new PropertyValueFactory<>("nomeMedico"));
    data.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDataString()));
    horario.setCellValueFactory(new PropertyValueFactory<>("horario"));
    status.setCellValueFactory(new PropertyValueFactory<>("status"));
    avaliacao.setCellValueFactory(new PropertyValueFactory<>("avaliacaoEstrelas"));

    consultas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            boolean podeAvaliar = "Concluída".equals(newSelection.getStatus()) &&
                                   newSelection.getAvaliacao() == 0;
            botaoAvaliar.setDisable(!podeAvaliar);
        } else {
            botaoAvaliar.setDisable(true);
        }
    });
}


    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        labelNomePaciente.setText(paciente.getNome());
        carregarConsultas();
    }
    
    private void carregarConsultas() {
        List<Consulta> todas = ConsultaDao.carregarConsultas();
        List<Consulta> minhas = new ArrayList<>();

        for (Consulta c : todas) {
            if (c.getNomePaciente().equalsIgnoreCase(paciente.getNome())) {
                minhas.add(c);
            }
        }
        
        consultas.setItems(FXCollections.observableArrayList(minhas));
    }
    
    @FXML
    public void handleAvaliar() {
        Consulta consultaSelecionada = consultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada != null) {
            System.out.println("Abrindo avaliação para: " + consultaSelecionada); // Depuração
            abrirJanelaAvaliacao(consultaSelecionada);
        } else {
            System.out.println("Nenhuma consulta selecionada para avaliação"); // Depuração
        }
    }
    
    private void abrirJanelaAvaliacao(Consulta consulta) {
    Stage stageAtual = (Stage) botaoAvaliar.getScene().getWindow();
    Navegacao.trocarTela(stageAtual, "/view/AvaliacaoDialog.fxml", "Avaliação",
        controller -> {
            // Correção: o controller é do tipo AvaliacaoDialogController
            if (controller instanceof AvaliacaoDialogController) {
                ((AvaliacaoDialogController) controller).setConsulta(consulta);
            } else {
                System.err.println("Erro: Controller não é do tipo esperado. Tipo real: " 
                                  + (controller != null ? controller.getClass() : "null"));
            }
        });
}

    @FXML
    public void handleVoltar() {
        Stage atual = (Stage) voltar.getScene().getWindow();
        Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
            ((PacienteController) controller).setPaciente(paciente);
        });
    }

    @FXML
    public void handleCancelarConsulta() {
        Consulta consultaSelecionada = consultas.getSelectionModel().getSelectedItem();
        if(consultaSelecionada != null) {
            consultas.getItems().remove(consultaSelecionada);
            ConsultaDao.removerConsultaDoArq(consultaSelecionada);
            LocalDate dataString = consultaSelecionada.getData();
            ConsultaDao.promoverPacienteDaListaEspera(
                    consultaSelecionada.getNomeMedico(),
                    dataString
            );
            setPaciente(paciente);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao cancelar consulta");
            alert.setContentText("Por favor, selecione uma consulta para cancelar.");
            alert.showAndWait();
        }
    }
}

