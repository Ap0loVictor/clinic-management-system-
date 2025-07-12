package clinicamed.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class Navegacao {

    public interface ControllerConfigurador {
        void configurar(Object controller);
    }

    public static void voltarParaLogin(Stage stageAtual) {
        trocarTela(stageAtual, "/view/Login.fxml", "Clinica Médica Fiquei Bom");
    }

     public static void trocarTela(Stage stageAtual, String fxmlPath, String titulo) {
        trocarTela(stageAtual, fxmlPath, titulo, null);
    }

    public static void trocarTela(Stage stageAtual, String fxmlPath, String titulo, ControllerConfigurador configurador) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacao.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            if (configurador != null) {
                configurador.configurar(loader.getController());
            }
            
            Stage novoStage = new Stage();
            novoStage.setTitle(titulo);
            novoStage.setScene(new Scene(root));
            novoStage.show();
            
            stageAtual.close();
        } catch (IOException e) {
            mostrarErro("Falha na navegação", "Erro ao carregar: " + fxmlPath);
        }
    }
    
    private static void mostrarErro(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
