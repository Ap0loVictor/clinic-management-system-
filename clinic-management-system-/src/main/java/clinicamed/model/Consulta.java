package clinicamed.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Consulta {
    private String nomePaciente;
    private String nomeMedico;
    private LocalDate data;
    private String horario;
    private String status;
    private String descricao;
    private int avaliacao;
    private String comentarioAvaliacao;

    // Formato padrão para datas (ISO: yyyy-MM-dd)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor principal agora recebe LocalDate
    public Consulta(String nomePaciente, String nomeMedico, LocalDate data,
                    String horario, String status, String descricao) {
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.descricao = descricao;
        this.avaliacao = 0;
        this.comentarioAvaliacao = "";
    }

    // Construtor sobrecarregado que aceita String para data (para compatibilidade)
    public Consulta(String nomePaciente, String nomeMedico, String dataStr,
                    String horario, String status, String descricao) {
        this(nomePaciente, nomeMedico, LocalDate.parse(dataStr, DATE_FORMATTER),
                horario, status, descricao);
    }
    public String getNomePaciente() { return nomePaciente; }
    public String getNomeMedico() { return nomeMedico; }
    public LocalDate getData() { return data; }
    public String getDataString() {
        return data.format(DATE_FORMATTER);
    }
    public String getHorario() { return horario; }
    public String getStatus() { return status; }
    public String getDescricao() { return descricao; }
    public int getAvaliacao() { return avaliacao; }
    public String getComentarioAvaliacao() { return comentarioAvaliacao; }
    public void setStatus(String status) { this.status = status; }
    public void setData(LocalDate data) { this.data = data; }
    public void setData(String dataStr) {
        this.data = LocalDate.parse(dataStr, DATE_FORMATTER);
    }
    public void setDescricao(String descricao) {
    this.descricao = descricao;
    }

    public void setAvaliacao(int avaliacao) { this.avaliacao = avaliacao; }
    public void setComentarioAvaliacao(String comentario) { this.comentarioAvaliacao = comentario; }
    public String getAvaliacaoEstrelas() {
        if(avaliacao == 0) return "Não avaliado";
        return "★".repeat(avaliacao);
    }
}