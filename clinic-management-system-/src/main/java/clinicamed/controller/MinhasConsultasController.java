package clinicamed.controller;

import clinicamed.model.Consulta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MinhasConsultasController extends Basecontroller implements Initializable {

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

    }
}
