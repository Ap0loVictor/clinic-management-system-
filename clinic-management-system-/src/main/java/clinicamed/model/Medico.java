package clinicamed.model;

public class Medico extends Usuario {
    private String especialidade;
    private String planoSaude;
    private double avaliacao;
    public Medico(String nome, String senha, String especialdiade, String planoSaude) {
        super(nome, senha);
        this.especialidade = especialdiade;
        this.planoSaude = planoSaude;
        this.avaliacao = 0.0; // Valor padrão
    }
     public double getAvaliacao() { return avaliacao; }
    
    public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
    public String getAvaliacaoString() {
        return String.valueOf(avaliacao);
    }
    // Na classe Medico
    public String getAvaliacaoEstrelas() {
        int estrelas = (int) Math.round(avaliacao);
        return "★".repeat(estrelas);
    }
    @Override
    public void setSenha(String senha) {
        super.setSenha(senha);
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }
    public void setEspecialidade(String especialdiade) {
        this.especialidade = especialdiade;
    }

    public void setPlanoSaude(String planoSaude) {
        this.planoSaude = planoSaude;
    }
    @Override
    public String getSenha() {
        return super.getSenha();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getPlanoSaude() {
        return planoSaude;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "especialidade='" + especialidade + '\'' +
                ", planoSaude='" + planoSaude + '\'' +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
