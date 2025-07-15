// AtenderConsultaController.java
package clinicamed.controller;

import java.io.IOException;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AtenderConsultaController {

    @FXML private Label labelPaciente;
    @FXML private Label labelPlano;
    @FXML private TextArea campoRelatorio;
    @FXML private Button botaoFinalizar;

    private Consulta consulta;
    private Paciente paciente;

    public void setConsulta(Consulta consulta, Paciente paciente) {
        this.consulta = consulta;
        this.paciente = paciente;

        labelPaciente.setText(consulta.getNomePaciente());
        labelPlano.setText(paciente.temPlano() ? "Sim" : "Não");
    }

    @FXML
    private void handleFinalizarConsulta() {
        String relatorio = campoRelatorio.getText();
        if (relatorio.isEmpty()) {
            mostrarAlerta("Digite o relatório da consulta antes de finalizar.");
            return;
        }

        // consulta.setDescricao(relatorio);
        consulta.setStatus("Concluída");
        ConsultaDao.atualizarConsulta(consulta);
        ConsultaDao.promoverPacienteDaListaEspera(consulta.getNomeMedico(), consulta.getData());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResultadoConsulta.fxml"));
            Parent root = loader.load();

            ResultadoConsultaController controller = loader.getController();
            controller.setDados(consulta, paciente);

            Stage resultadoStage = new Stage();
            resultadoStage.setTitle("Resultado da Consulta");
            resultadoStage.setScene(new Scene(root));
            resultadoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        Stage stage = (Stage) botaoFinalizar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
