package clinicamed.model;

public class Paciente extends Usuario {
    private int idade;
    private String planoSaude;

    public Paciente(String nome, String senha, int idade, String planoSaude) {
        super(nome, senha);
        this.idade = idade;
        this.planoSaude = planoSaude;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(String planoSaude) {
        this.planoSaude = planoSaude;
    }

    /**
     * Método de apoio para verificar se o paciente tem plano de saúde.
     * @return true se tiver plano diferente de "Não", false caso contrário.
     */
    public boolean temPlano() {
        return planoSaude != null && !planoSaude.equalsIgnoreCase("Não");
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nome='" + getNome() + '\'' +
                ", senha='" + getSenha() + '\'' +
                ", idade=" + idade +
                ", planoSaude='" + planoSaude + '\'' +
                '}';
    }
}
