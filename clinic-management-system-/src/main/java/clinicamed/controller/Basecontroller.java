package clinicamed.controller;
import clinicamed.utils.Navegacao;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Basecontroller {
    protected abstract Button getBotaoSair();
    
    public void handleSair() {
        Stage stageAtual = (Stage) getBotaoSair().getScene().getWindow();
        Navegacao.voltarParaLogin(stageAtual);
    }
}
