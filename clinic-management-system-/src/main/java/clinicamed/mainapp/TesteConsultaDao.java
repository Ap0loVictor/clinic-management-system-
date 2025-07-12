package clinicamed.mainapp;

import clinicamed.dao.ConsultaDao;
import clinicamed.model.Consulta;

import java.util.ArrayList;

public class TesteConsultaDao {
    public static void main(String[] args) {
        
        Consulta novaConsulta = new Consulta(
                "Apolo Victor",
                "Guilherme Gaspar",
                "2025-07-20",
                "16:00",
                "Pendente",
                "depressão :("
        );

        ConsultaDao.salvarConsulta(novaConsulta);
        System.out.println("Consulta salva!");

        ArrayList<Consulta> consultas = ConsultaDao.carregarConsultas();
        System.out.println("\nConsultas carregadas do arquivo:");

        for (Consulta c : consultas) {
            System.out.println("Médico: " + c.getNomeMedico());
            System.out.println("Paciente: " + c.getNomePaciente());
            System.out.println("Data: " + c.getData());
            System.out.println("Horário: " + c.getHorario());
            System.out.println("Status: " + c.getStatus());
            System.out.println("Descrição: " + c.getDescricao());
            System.out.println("---------------------------");
        }
    }
}
