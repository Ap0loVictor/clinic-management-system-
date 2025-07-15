package clinicamed.model;

public class Consulta {
    private String nomePaciente;
    private String nomeMedico;
    private String data;
    private String horario;
    private String status; // Agendada, Realizada, Cancelada
    private String descricao;
    private int avaliacao; // 0-5 (0 = não avaliado)
    private String comentarioAvaliacao;

    public Consulta(String nomePaciente, String nomeMedico, String data, String horario, String status, String descricao) {
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.descricao = descricao;
        this.avaliacao = 0;
        this.comentarioAvaliacao = "";
    }

    // Getters e Setters
    public String getNomePaciente() { return nomePaciente; }
    public String getNomeMedico() { return nomeMedico; }
    public String getData() { return data; }
    public String getHorario() { return horario; }
    public String getStatus() { return status; }
    public String getDescricao() { return descricao; }
    public int getAvaliacao() { return avaliacao; }
    public String getComentarioAvaliacao() { return comentarioAvaliacao; }
    
    public void setStatus(String status) { this.status = status; }
    public void setAvaliacao(int avaliacao) { this.avaliacao = avaliacao; }
    public void setComentarioAvaliacao(String comentario) { this.comentarioAvaliacao = comentario; }
    
    // Método para converter avaliação em estrelas
    public String getAvaliacaoEstrelas() {
        if(avaliacao == 0) return "Não avaliado";
        return "★".repeat(avaliacao);
    }
    
    
}