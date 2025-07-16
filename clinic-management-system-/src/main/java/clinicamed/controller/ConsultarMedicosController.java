package clinicamed.controller;

import clinicamed.dao.MedicoDao;
import clinicamed.model.Medico;
import clinicamed.model.Paciente;
import clinicamed.utils.Navegacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ConsultarMedicosController {
        
    private Paciente paciente;
    
    @FXML private RadioButton radioNome;
    @FXML private RadioButton radioEspecialidade;
    @FXML private TextField campoBusca;
    @FXML private Button botaoBuscar;
    @FXML private Label labelInfoPlano;
    @FXML private TableView<Medico> tabelaMedicos;
    @FXML private TableColumn<Medico, String> colunaNome;
    @FXML private TableColumn<Medico, String> colunaEspecialidade;
    @FXML private TableColumn<Medico, String> colunaAvaliacao;
    @FXML private TableColumn<Medico, String> colunaPlanos;
    @FXML private Button botaoVoltar; // Adicionado @FXML

    private ObservableList<Medico> todosMedicos;
    private FilteredList<Medico> medicosFiltrados;


    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        configurarFiltroPlano();
        atualizarLabelPlano();
    }

    @FXML
    public void initialize() {
        configurarColunasTabela();
        carregarMedicos();
        configurarFiltros();
        
        botaoBuscar.setOnAction(e -> filtrarMedicos());
        campoBusca.setOnAction(e -> filtrarMedicos());
        botaoVoltar.setOnAction(e -> handleVoltar()); // Configurar ação do botão
    }

    private void configurarColunasTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        
        // Usando o novo getter
        colunaAvaliacao.setCellValueFactory(new PropertyValueFactory<>("avaliacaoEstrelas"));
        
        // Configuração simplificada
        colunaAvaliacao.setCellFactory(column -> new TableCell<Medico, String>() {
            @Override
            protected void updateItem(String estrelas, boolean empty) {
                super.updateItem(estrelas, empty);
                setText(empty ? null : estrelas);
            }
        });
        
        colunaPlanos.setCellValueFactory(new PropertyValueFactory<>("planoSaude"));
    }



    private void carregarMedicos() {
        todosMedicos = FXCollections.observableArrayList(MedicoDao.carregarMedicos());
        medicosFiltrados = new FilteredList<>(todosMedicos);
        tabelaMedicos.setItems(medicosFiltrados);
    }

    private void configurarFiltros() {
        // Listener para mudanças no campo de busca
        campoBusca.textProperty().addListener((obs, oldVal, newVal) -> {
            filtrarMedicos();
        });
        
        // Listener para mudança no tipo de busca
        radioNome.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) filtrarMedicos();
        });
        
        radioEspecialidade.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) filtrarMedicos();
        });
    }

    private void configurarFiltroPlano() {
        if (paciente == null || paciente.getPlanoSaude() == null || 
            paciente.getPlanoSaude().isEmpty() || 
            paciente.getPlanoSaude().equalsIgnoreCase("Particular")) {
            
            // Paciente sem plano ou com plano particular: mostrar todos os médicos
            medicosFiltrados.setPredicate(null);
        } else {
            // Paciente com plano específico: mostrar apenas médicos que atendem esse plano
            final String planoPaciente = paciente.getPlanoSaude().toLowerCase();
            medicosFiltrados.setPredicate(medico -> 
                medico.getPlanoSaude().toLowerCase().contains(planoPaciente)
            );
        }
    }

    private void atualizarLabelPlano() {
        if (paciente != null && paciente.getPlanoSaude() != null && 
            !paciente.getPlanoSaude().isEmpty() && 
            !paciente.getPlanoSaude().equalsIgnoreCase("Particular")) {
            
            labelInfoPlano.setText("Mostrando médicos que atendem seu plano: " + paciente.getPlanoSaude());
            labelInfoPlano.setVisible(true);
        } else {
            labelInfoPlano.setVisible(false);
        }
    }

    private void filtrarMedicos() {
        String termoBusca = campoBusca.getText().toLowerCase();
        
        if (termoBusca.isEmpty()) {
            configurarFiltroPlano();
            return;
        }
        
        medicosFiltrados.setPredicate(medico -> {
            // Primeiro aplica o filtro de plano
            boolean atendePlano;
            if (paciente == null || paciente.getPlanoSaude() == null || 
                paciente.getPlanoSaude().isEmpty() || 
                paciente.getPlanoSaude().equalsIgnoreCase("Particular")) {
                
                atendePlano = true; // Sem restrição de plano
            } else {
                String planoPaciente = paciente.getPlanoSaude().toLowerCase();
                atendePlano = medico.getPlanoSaude().toLowerCase().contains(planoPaciente);
            }
            
            if (!atendePlano) return false;
            
            // Depois aplica o filtro de busca
            if (radioNome.isSelected()) {
                return medico.getNome().toLowerCase().contains(termoBusca);
            } else if (radioEspecialidade.isSelected()) {
                return medico.getEspecialidade().toLowerCase().contains(termoBusca);
            }
            return true;
        });
    }
    
    @FXML
    private void handleVoltar() {
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();
        Navegacao.trocarTela(stage, "/view/TelaPaciente.fxml", "Área do Paciente", controller -> {
            if (controller instanceof PacienteController) {
                ((PacienteController) controller).setPaciente(paciente);
            }
        });
    }
}