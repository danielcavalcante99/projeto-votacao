
### Requisitos

- 1 Docker instalado
  - Kafka e MongoDB rodando na máquina

- 2 Nesse repositorio mesmo, primeiramente acessar a partir do diretório raiz ./api-votacao
  - Executar o projeto dentro ./api-votacao 
  - Nesse projeto microservico-votacao setar a variável de ambiente com endereço do projeto api-votacao
    DOMAIN_VOTACAO_API=http://localhost:8080
    
 ### Sobre o projeto
 
- Cadastro de Associado;
- Cadastro de uma nova pauta;
- Sessão de votação em uma pauta 5 minuto por default para o encerramento;
-	Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
-	Contabiliza os votos e dar o resultado da votação na pauta.
