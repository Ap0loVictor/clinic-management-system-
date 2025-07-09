package clinicamed.model;

public class Medico extends Usuario {
    private String especialdiade;
    private String planoSaude;
    public Medico(String nome, String senha, String especialdiade, String planoSaude) {
        super(nome, senha);
        this.especialdiade = especialdiade;
        this.planoSaude = planoSaude;
    }

    @Override
    public void setSenha(String senha) {
        super.setSenha(senha);
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }
    public void setEspecialdiade(String especialdiade) {
        this.especialdiade = especialdiade;
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

    public String getEspecialdiade() {
        return especialdiade;
    }

    public String getPlanoSaude() {
        return planoSaude;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "especialdiade='" + especialdiade + '\'' +
                ", planoSaude='" + planoSaude + '\'' +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
