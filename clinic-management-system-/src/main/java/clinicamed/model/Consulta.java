package clinicamed.model;

public class Consulta {
    private String nomeMedico;
    private String nomePaciente;
    private String data;
    private String horario;
    private String status;
    private String descricao;

    public Consulta(String nomeMedico, String nomePaciente, String data, String horario, String status, String descricao) {
        this.nomeMedico = nomeMedico;
        this.nomePaciente = nomePaciente;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.descricao = descricao;
    }

    public String getNomeMedico(){
        return nomeMedico;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public String getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
    this.descricao = descricao;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
