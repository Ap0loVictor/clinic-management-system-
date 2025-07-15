// package clinicamed.controller;

// import clinicamed.dao.MedicoDao;
// import clinicamed.model.Medico;
// import clinicamed.model.Paciente;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.collections.transformation.FilteredList;
// import javafx.fxml.FXML;
// import javafx.scene.control.*;
// import javafx.scene.control.cell.PropertyValueFactory;

// public class ConsultarMedicosController{
        
//     private Paciente paciente;
    
//     @FXML private RadioButton radioNome;
//     @FXML private RadioButton radioEspecialidade;
//     @FXML private TextField campoBusca;
//     @FXML private Button botaoBuscar;
//     @FXML private Label labelInfoPlano;
//     @FXML private TableView<Medico> tabelaMedicos;
//     @FXML private TableColumn<Medico, String> colunaNome;
//     @FXML private TableColumn<Medico, String> colunaEspecialidade;
//     @FXML private TableColumn<Medico, String> colunaAvaliacao;
//     @FXML private TableColumn<Medico, String> colunaPlanos;
//     @FXML private Button botaoVoltar;

//     private ObservableList<Medico> todosMedicos;
//     private FilteredList<Medico> medicosFiltrados;


//     public void setPaciente(Paciente paciente) {
//         this.paciente = paciente;
//         configurarFiltroPlano();
//         atualizarLabelPlano();
//     }

//     @FXML
//     public void initialize() {
//         configurarColunasTabela();
//         carregarMedicos();
//         configurarFiltros();
        
//         botaoBuscar.setOnAction(e -> filtrarMedicos());
//         campoBusca.setOnAction(e -> filtrarMedicos());
//     }

//     private void configurarColunasTabela() {
//         colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
//         colunaEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        
//         // Formatar avaliação como estrelas
//         colunaAvaliacao.setCellValueFactory(new PropertyValueFactory<>("avaliacao"));
//         colunaAvaliacao.setCellFactory(column -> new TableCell<Medico, String>() {
//             @Override
//             protected void updateItem(String item, boolean empty) {
//                 super.updateItem(item, empty);
                
//                 if (empty || item == null) {
//                     setText(null);
//                 } else {
//                     try {
//                         double avaliacao = Double.parseDouble(item);
//                         setText("★".repeat((int) Math.round(avaliacao)));
//                     } catch (NumberFormatException e) {
//                         setText(item);
//                     }
//                 }
//             }
//         });
        
//         colunaPlanos.setCellValueFactory(new PropertyValueFactory<>("planoSaude"));
//     }

//     private void carregarMedicos() {
//         todosMedicos = FXCollections.observableArrayList(MedicoDao.listarTodos());
//         medicosFiltrados = new FilteredList<>(todosMedicos);
//         tabelaMedicos.setItems(medicosFiltrados);
//     }

//     private void configurarFiltros() {
//         // Listener para mudanças no campo de busca
//         campoBusca.textProperty().addListener((obs, oldVal, newVal) -> {
//             filtrarMedicos();
//         });
        
//         // Listener para mudança no tipo de busca
//         radioNome.selectedProperty().addListener((obs, oldVal, newVal) -> {
//             if (newVal) filtrarMedicos();
//         });
        
//         radioEspecialidade.selectedProperty().addListener((obs, oldVal, newVal) -> {
//             if (newVal) filtrarMedicos();
//         });
//     }

//     private void configurarFiltroPlano() {
//         if (paciente != null && paciente.isTemPlano()) {
//             medicosFiltrados.setPredicate(medico -> 
//                 medico.getPlanoSaude().contains(paciente.getPlanoSaude())
//             );
//         } else {
//             medicosFiltrados.setPredicate(null); // Mostrar todos
//         }
//     }

//     private void atualizarLabelPlano() {
//         if (paciente != null && paciente.isTemPlano()) {
//             labelInfoPlano.setText("Mostrando médicos que atendem seu plano: " + paciente.getPlanoSaude());
//             labelInfoPlano.setVisible(true);
//         } else {
//             labelInfoPlano.setVisible(false);
//         }
//     }

//     private void filtrarMedicos() {
//         String termoBusca = campoBusca.getText().toLowerCase();
        
//         if (termoBusca.isEmpty()) {
//             configurarFiltroPlano();
//             return;
//         }
        
//         medicosFiltrados.setPredicate(medico -> {
//             // Primeiro aplica o filtro de plano
//             boolean atendePlano = paciente == null || !paciente.isTemPlano() || 
//                                  medico.getPlanoSaude().contains(paciente.getPlanoSaude());
            
//             if (!atendePlano) return false;
            
//             // Depois aplica o filtro de busca
//             if (radioNome.isSelected()) {
//                 return medico.getNome().toLowerCase().contains(termoBusca);
//             } else if (radioEspecialidade.isSelected()) {
//                 return medico.getEspecialidade().toLowerCase().contains(termoBusca);
//             }
//             return true;
//         });
//     }    
// }
