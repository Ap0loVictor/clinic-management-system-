package clinicamed.controller;

import clinicamed.model.Consulta;
import clinicamed.model.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ResultadoConsultaController {

    @FXML private Label labelNomePaciente;
    @FXML private Label labelNomeMedico;
    @FXML private Label labelData;
    @FXML private Label labelPlano;
    @FXML private Label labelValor;

    public void setDados(Consulta consulta, Paciente paciente) {
    labelNomePaciente.setText(consulta.getNomePaciente());
    labelNomeMedico.setText(consulta.getNomeMedico());
    labelData.setText(consulta.getData());

    double valorConsulta = 1600.0;

    if (paciente.temPlano() && !paciente.getPlanoSaude().equalsIgnoreCase("Não tenho")) {
        labelPlano.setText(paciente.getPlanoSaude());
        labelValor.setText("R$ " + valorConsulta + ", mas o plano " + paciente.getPlanoSaude() + " cobriu os custos.");
    } else {
        labelPlano.setText("Não possui plano de saúde");
        labelValor.setText("Valor total: R$ " + valorConsulta);
    }
}


    @FXML
    private void handleFechar() {
        Stage stage = (Stage) labelValor.getScene().getWindow();
        stage.close();
    }
}