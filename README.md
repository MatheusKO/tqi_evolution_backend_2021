# tqi_evolution_backend_2021
Projeto de gerenciamento de empréstimos para o processo seletivo TQI Evolution 2022.

Coleção de Requests do Postman: https://www.getpostman.com/collections/fef354cab7a1e576007a.

O banco de dados utilizado foi o Postgres através do Docker.

```shell script
docker run --name loans-db -d -p 5432:5432 -e POSTGRES_USER=super_user e POSTGRES_PASSWORD=super_password -e POSTGRES_DB=loans postgres
```

Para popular o banco, pode-se executar os POST requests da coleção do Postman.

Quanto ao login, embora ele seja feito com o Authorization Header e retorne um JWT, não consegui configurar corretamente o Spring Security, então mesmo com os arquivos estando no repositório, deixei desabilitado na main do programa. Para ter o mínimo de simulação de autenticação nas rotas protegidas, usei o Authorization Header, o que deixou o código bem bagunçado (Corrigir isso fica como um próximo passo para mim).

A validação foi feita como pedida, com o Spring Validator.

O retorno dos request são do tipo ResponseEntity, mas eu gostaria de padronizar ainda mais os retornos, principalmente de exceções.

A parte mais díficil provavelmente foi realizar as queries com retorno de colunas específicas na consulta de empréstimos e conseguir retornar um JSON. Tentei soluções que não envolvessem criar outras classes de apoio, mas acabei com duas classes só para retornos específicos.

Uma outra coisa que eu gostaria de implementar é um Logger e a documentação do Swagger, já que infelizmente não me organizei corretamente para fazer esse projeto e lidar com os imprevistos.
