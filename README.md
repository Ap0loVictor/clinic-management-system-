- Dicas Importantes:

    - O trabalho é extenso, portanto não deixe para última hora
    - Procure compreender como funciona o sistema que você irá implementar
    - Não comece a implementar logo o sistema. 
    - Procure antes descrever o algoritmo, desenhar classes e projetar interfaces primeiro
    - Caso julgue necessário, você pode criar outras entidades para o desenvolvimento do sistema 
    - Qualquer dúvida sobre o trabalho, entre em contato com o professor

---

- Considerações inicias:

    - Deve-se usar armazenamento das informações em arquivos csv ou txt e interface gráfica para entrada e saída das informações. 
    - Use também exceções para lidar com situações não desejadas ou inesperadas.
    - As descrições abaixo representam o mínimo que o sistema deve conter. Usem a criatividade para incorporar novidades (novas funcionalidades e atributos) no sistema. Tudo que vier a mais contará positivamente. 

---

# Sistema de Consultas Médicas
## Entidades:

- Médico
    - Nome
    - Especialidade
    - Plano de saúde que atende
- Paciente
    - Nome
    - Idade
    - Plano de Saúde (marcar como “não tenho” caso o paciente não tenha)

## Operações:

1. Realizar login de médico e paciente.
2. Apenas médicos podem alterar seus dados, o mesmo para usuários. 
3. Agendar uma consulta.
4. Cancelar um agendamento.
5. Realizar uma consulta.
6. Avaliar uma consulta

---

Regras:
O paciente pode verificar todos os médicos, pesquisando por nome, especialidade. O sistema apresenta nome, especialidade, quantidade de estrelas do médico e últimas avaliações. O sistema mostra apenas os médicos que atendem ao plano de saúde do paciente. Caso ele não tenha plano de saúde, o sistema mostra todos os médicos.
Para agendar uma consulta, seleciona-se o médico e a data. Um médico só atende até três pacientes no mesmo dia. Se o médico estiver com os horários lotados em um dia, o paciente pode ir para uma lista de espera.
Para cancelar um agendamento, se houver pacientes na lista de espera daquele médico naquele dia, ele é alocado no lugar do paciente cancelado.
Para realizar uma consulta, o médico informa a descrição do que aconteceu na consulta (sintomas do paciente, tratamento sugerido, medicamentos, exames...).  Se o paciente tem plano de saúde, ele não paga nada, mas se ele não tem, é gerado uma conta para ele pagar de acordo com o médico e a especialidade dele.
Após a consulta, o paciente pode fazer uma avaliação do médico, escrevendo um texto e dando de 1 a 5 estrelas para ele.