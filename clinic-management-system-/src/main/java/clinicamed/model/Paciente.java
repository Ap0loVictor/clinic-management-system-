package clinicamed.model;

public class Paciente extends Usuario{
    private int idade;
    private boolean temPlano;

    public Paciente(String nome, String senha, int idade, boolean temPlano) {
        super(nome, senha);
        this.idade = idade;
        this.temPlano = temPlano;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public void setSenha(String senha) {
        super.setSenha(senha);
    }

    public void setTemPlano(boolean temPlano) {
        this.temPlano = temPlano;
    }

    public boolean isTemPlano() {
        return temPlano;
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
                "temPlano=" + temPlano +
                ", idade=" + idade +
                ", senha='" + senha + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
