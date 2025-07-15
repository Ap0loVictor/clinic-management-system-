package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MinhasConsultasController extends Basecontroller implements Initializable {

    private Paciente paciente;
    @FXML private Button voltar;
    @FXML private TableView<Consulta> consultas;
    @FXML private TableColumn<Consulta, String> nomeMedico;
    @FXML private TableColumn<Consulta, String> status;
    @FXML private TableColumn<Consulta, String> horario;
    @FXML private TableColumn<Consulta, String> data;
    @FXML private TableColumn<Consulta, String> descricao;
    @FXML private Button cancelarConsulta;
    @Override
    protected Button getBotaoSair() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomeMedico.setCellValueFactory(new PropertyValueFactory<>("nomeMedico"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        horario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        List<Consulta> todas = ConsultaDao.carregarConsultas();
        List<Consulta> minhas = new ArrayList<>();

        for (Consulta c : todas) {
            if (c.getNomePaciente().equalsIgnoreCase(paciente.getNome())) {
                minhas.add(c);
            }
        }
        consultas.getItems().setAll(minhas);
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
            removerConsultaDoArq(consultaSelecionada);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao cancelar consulta");
            alert.setContentText("Por favor, selecione uma consulta para cancelar.");
            alert.showAndWait();
        }
    }
    public void removerConsultaDoArq(Consulta consulta) {
        Path path = Paths.get("consultas.txt");
        try {
            List<String> linhas = Files.readAllLines(path);
            List<String> novasLinhas = new ArrayList<>();
            for (String linha : linhas) {
                String[] partes = linha.split("\t");
                if(partes.length == 6) {
                    String nomeMedico = partes[0].trim();
                    String nomePaciente = partes[1].trim();
                    String data = partes[2].trim();
                    String horario = partes[3].trim();
                    String status = partes[4].trim();
                    String descricao = partes[5].trim();
                    boolean eigual = nomeMedico.equals(consulta.getNomeMedico()) &&
                                     nomePaciente.equals(consulta.getNomePaciente()) &&
                                     data.equals(consulta.getData()) && horario.equals(consulta.getHorario()) &&
                                     status.equals(consulta.getStatus()) && descricao.equals(consulta.getDescricao());
                    if(!eigual) {
                        novasLinhas.add(linha);
                    }
                }
            }
            Files.write(path, novasLinhas);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao cancelar consulta");
            alert.setContentText("Não foi possível atualizar o arquivo de consultas.");
            alert.showAndWait();
        }
    }
}
