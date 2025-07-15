package clinicamed.controller;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AvaliacaoDialogController {

    @FXML private ComboBox<Integer> cmbAvaliacao;
    @FXML private TextArea txtComentario;
    
    private Consulta consulta;

    @FXML
    public void initialize() {
        cmbAvaliacao.getItems().setAll(1, 2, 3, 4, 5);
    }
    
    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
        System.out.println("Consulta configurada: " + consulta); // Para depuração
        
        // Atualiza o status para "Realizada" se necessário
        if (!"Realizada".equals(consulta.getStatus())) {
            consulta.setStatus("Realizada");
        }
    }
    
    @FXML
    private void handleSalvar() {
        if (cmbAvaliacao.getValue() == null) {
            mostrarAlerta("Atenção", "Selecione uma avaliação de 1 a 5 estrelas");
            return;
        }
        
        consulta.setAvaliacao(cmbAvaliacao.getValue());
        consulta.setComentarioAvaliacao(txtComentario.getText());
        
        ConsultaDao.atualizarConsulta(consulta);
        
        mostrarAlerta("Sucesso", "Avaliação registrada com sucesso!");
        fecharJanela();
    }
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) cmbAvaliacao.getScene().getWindow();
        stage.close();
    }
}