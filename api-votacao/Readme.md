
### Requisitos

- 1 Docker instalado
  - Kafka e MongoDB rodando na máquina

- 2 Clonar projeto validador-cpf: https://github.com/danielcavalcante99/validador-cpf/tree/master
  - após clonar executar o projeto validador-cpf
  - Nesse projeto api-votacao setar a variável de ambiente com endereço do projeto validador-cpf
    DOMAIN_VALIDATOR_CEP_API=http://localhost:8787
    
 ### Sobre o projeto
 
- Cadastro de Associado;
- Cadastro de uma nova pauta;
- Sessão de votação em uma pauta 5 minuto por default para o encerramento;
-	Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
-	Contabiliza os votos e dar o resultado da votação na pauta;
- Esse projeto consome API Rest do projeto <b>validador-cpf</b> que verifica se o CPF dos associados são válido.
