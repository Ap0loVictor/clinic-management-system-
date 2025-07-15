package clinicamed.model;

public class Paciente extends Usuario{
    private int idade;
    private String planoSaude;

    public Paciente(String nome, String senha, int idade, String planoSaude) {
        super(nome, senha);
        this.idade = idade;
        this.planoSaude = planoSaude;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public void setSenha(String senha) {
        super.setSenha(senha);
    }

    public String getPlanoSaude() {
        return planoSaude;
    }
    public void setPlanoSaude(String planoSaude) {
        this.planoSaude = planoSaude;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getSenha() {
        return super.getSenha();
    }

    public int getIdade() {
        return idade;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "planoSaude=" + planoSaude +
                ", idade=" + idade +
                ", senha='" + senha + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
