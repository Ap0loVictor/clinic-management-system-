package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.xml.stream.XMLInputFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MinhasConsultasController extends Basecontroller implements Initializable {
    private Paciente paciente;
    @FXML private Button voltar;
    @FXML private TableView<Consulta> consultas;
    @FXML private TableColumn<Consulta, String> nomeMedico;
    @FXML private  TableColumn<Consulta, String> status;
    @FXML private TableColumn<Consulta, String> horario;
    @FXML private TableColumn<Consulta, String> data;
    @FXML private TableColumn<Consulta, String> descricao;
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
        if(paciente != null) {
            List<Consulta> minhasConsultas = ConsultaDao.buscarPorPaciente(paciente.getNome());
            consultas.getItems().setAll(minhasConsultas);
        }
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        List<Consulta> allConsultas = ConsultaDao.carregarConsultas();
        List<Consulta> myConsultas = new ArrayList<>();
        for (Consulta consulta : allConsultas) {
            if(consulta.getNomePaciente().equalsIgnoreCase(paciente.getNome())) {
                myConsultas.add(consulta);
            }
        }
        consultas.getItems().setAll(myConsultas);
    }
    @FXML
    public void handleVoltar() {
        Stage atual = (Stage) voltar.getScene().getWindow();
        Navegacao.trocarTela(atual, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
            ((PacienteController) controller).setPaciente(paciente);
        });
    }
}
