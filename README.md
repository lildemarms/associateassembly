# associate-assembly

API REST para votação em assembleia

Este projeto é um desafio técnico, descrição:

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução para dispositivos móveis para gerenciar e participar dessas sessões de votação.

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java, usando Spring-boot, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

## Tarefas bônus

Tarefa Bônus 1 - Integração com sistemas externos

    Integrar com um sistema que verifique, a partir do CPF do associado, se ele pode votar
    GET https://user-info.herokuapp.com/users/{cpf}
    Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos;
    Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação Exemplos de retorno do serviço

Tarefa Bônus 2 - Performance

    Imagine que sua aplicação possa ser usada em cenários que existam centenas de milhares de votos. Ela deve se comportar de maneira performática nesses cenários;
    Testes de performance são uma boa maneira de garantir e observar como sua aplicação se comporta.

Tarefa Bônus 3 - Versionamento da API

    Como você versionaria a API da sua aplicação? Que estratégia usar?

## Stack utilizada

- Java 8
- Spring
- SLF4J
- Maven
- MongoDB
- ModelMapper
- Swagger
- JUnit
- Mockito
- Docker
- Nginx

## Requisitos

- Java 8
- Maven 3.6+
- Docker instalado na máquina

## Rodar a aplicação

O Docker deve estar rodando na máquina.

Clone o projeto e navegue até a pasta root do mesmo por um terminal.

Ative o profile desejado no application.properties (dev ou prod).

Então, execute os comandos na sequência abaixo para compilar, rodar os testes unitários da aplicação e gerar as imagens docker:

- mvn clean
- mvn package
- docker-compose up

Caso deseje rodar apenas os testes:
- mvn test

## Acessar a API

Após subir os containers do docker, a api ficará disponivel em:

- PROD: http://localhost/api/v1/
- DEV: http://localhost:8080/api/v1/

Para visualizar a documentação da api:

- PROD: http://localhost/swagger-ui.html
- DEV: http://localhost:8080/swagger-ui.html

OBS: Dependendo da instalação e configuração do docker para acessar a aplicação deverá ser utilizado o IP da VM do Docker Toolbox. 

#### Tarefa Bônus 1 - Integração com sistemas externos
    Foi implementado uma integração básica, 'CpfService', a qual realiza a integração com o serviço externo e trata as possíveis falhas.

#### Tarefa Bônus 2 - Performance
    ...

#### Tarefa Bônus 3 - Versionamento da API
    Há várias formas de fazer o versionamento, podemos versionar a API pela url, usando os cabeçalhos HTTP ou utilizando a combinação dos dois.
    Para este teste foi escolhido o versionamento pela url, onde a api inicial foi verionada da seguinte forma api/v1/.
    Surgindo a necessidade de uma nova versão, seria criado novos controllers e/ou services e seria gerado uma nova versão da api. Exemplo api/v2.
    
## Escolhas tomadas

#### MongoDB
    MongoDB é um banco de dados NoSQL, que usa um modelo de dados flexível, de modo que o schema podem mudar facilmente conforme a aplicação
    evolui. Para essa solução o MongoDB foi escolhido por:
    - Possuir schema flexível (Baseado em documentos)
    - Suporte escalabilidade horizontal (Multi-Datacenter) nativa
    - Performance de escrita in-memory
    

#### Swagger
	Swagger é uma ótima opção para documentação de API. O seu grande objetivo é permitir que a documentação possa evoluir no mesmo ritmo da 
	implementação, já que pode ser gerada automaticamente com base em anotações do código.

#### Docker
	O Docker o proporciona construir e gerenciar ambientes de diferentes tipos, automatizando instalações de sistema operacional, servidores, 
	banco de dados e outras dependências necessárias para executar a aplicação. Nesta solução o Docker é utilizado para distribuição da aplicação
	bem como todo o ambiente de execução. 
	
#### Nginx
	Nesta solução o Nginx foi usado do load balancer utilizando o algoritmo de balanceamento padrão (round-robin).