# AgroConecta — Sistema de Gestão e Conexão para Micro Produtores Rurais

Atividade de Estudo Programada (AEP) — 4º Semestre (2026.2) Engenharia de Software | Análise e Desenvolvimento de Sistemas — UniCesumar

**ODS Vinculadas:** ODS 2 — Fome Zero e Agricultura Sustentável; ODS 9 — Indústria, Inovação e Infraestrutura; ODS 12 — Consumo e Produção Responsáveis

**Equipe de Desenvolvimento:**

* João Guilherme Muller Bordin
* Matheus José de Azevedo
* Luiz Otávio Severino

## Sobre o projeto

O AgroConecta é um sistema de gestão agrícola voltado para micro produtores rurais. O sistema permite organizar o ciclo produtivo, desde o cadastro do produtor e terreno até o plantio, monitoramento, colheita e venda da produção.

O documento completo da 1ª entrega e os diagramas (classes e DER) estão em `/Docs`.

## Estrutura do repositório

```text
/src
  /app        → AgroConectaApp (ponto de entrada, método main)
  /model      → entidades do domínio (Usuario, Produtor, Terreno, Plantio, ...)
  /dao        → acesso ao MySQL via JDBC (GenericDAO, AbstractDAO e um DAO por tabela)
  /service    → regras de negócio e validações
  /util       → conexão com o banco, CPF, datas, senha (hash) e exceções
  /view       → menus do terminal
/Docs         → documento da 1ª entrega, Diagrama de Classes e DER
/database     → script SQL de criação das tabelas (schema.sql)
/config       → db.properties (usuário e senha do MySQL)
pom.xml       → build Maven (Java 17 + driver MySQL)
```

## Como executar

O projeto roda em **Windows, Linux e macOS**: é Java puro sobre a JVM, o driver do MySQL é baixado pelo Maven e os caminhos de arquivo são relativos à pasta do projeto.

### 1. Pré-requisitos

| Ferramenta | Versão | Como conferir |
| ---------- | ------ | ------------- |
| JDK        | 17 ou superior | `java -version` |
| Maven      | 3.8 ou superior | `mvn -v` |
| MySQL      | 8.0 ou superior | `mysql --version` |

> Se preferir, abra a pasta do projeto no IntelliJ IDEA, Eclipse ou NetBeans: todos reconhecem o `pom.xml` e já trazem o Maven embutido.

### 2. Criar o banco de dados

Na pasta do projeto, execute o script (ele cria o banco `agroconecta`, as tabelas e o administrador padrão):

```bash
mysql -u root -p < database/schema.sql
```

No Windows, sem o `mysql` no PATH, abra o MySQL Workbench, carregue `database/schema.sql` e execute com o raio ⚡.

O script pode ser executado mais de uma vez sem apagar dados.

### 3. Configurar o acesso ao banco

Edite `config/db.properties` com o usuário e a senha do seu MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/agroconecta?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
db.usuario=root
db.senha=SUA_SENHA
```

Como alternativa, defina as variáveis de ambiente `AGRO_DB_URL`, `AGRO_DB_USER` e `AGRO_DB_PASSWORD`. Elas têm prioridade sobre o arquivo.

### 4. Compilar e executar

Sempre a partir da pasta raiz do projeto, onde fica o `pom.xml`:

```bash
mvn package
java -jar target/agroconecta.jar
```

Ou, sem gerar o `.jar`:

```bash
mvn compile exec:java
```

No Windows, se os acentos aparecerem errados no terminal, rode `chcp 65001` antes de executar.

### 5. Primeiro acesso

| Perfil | CPF | Senha |
| ------ | --- | ----- |
| Administrador | `000.000.000-00` | `admin123` |

O produtor pode se cadastrar pela opção **"Sou produtor e quero me cadastrar"** da tela inicial. O CPF precisa ser válido.

## Lista de Requisitos

| Requisito | Descrição                                                                                                     |
| --------- | ------------------------------------------------------------------------------------------------------------- |
| RF01      | O sistema deve permitir o cadastro de produtores rurais, com nome completo, CPF, telefone, e-mail e endereço. |
| RF02      | O sistema deve permitir o cadastro de terrenos vinculados a um produtor.                                      |
| RF03      | O sistema deve permitir o registro de um plantio para um terreno.                                             |
| RF04      | O sistema deve permitir o registro das mudas utilizadas em um plantio.                                        |
| RF05      | O sistema deve permitir o registro de ocorrências de pragas, doenças e volume de chuva.                       |
| RF06      | O sistema deve permitir o registro da colheita de um plantio.                                                 |
| RF07      | O sistema deve permitir o cadastro de vendas vinculadas a uma colheita.                                       |
| RF08      | O sistema deve exibir ao produtor uma tela de avisos com pendências.                                          |

**Requisitos não funcionais:** usabilidade, simplicidade, segurança, desempenho e manutenibilidade.

### Onde cada requisito está implementado

| Requisito | Operações | Classes principais |
| --------- | --------- | ------------------ |
| RF01 | CRUD completo (pelo administrador e em "Meus dados") | `Produtor`, `ProdutorService`, `UsuarioDAO`, `MenuAdministrador`, `MenuMeusDados` |
| RF02 | CRUD completo | `Terreno`, `TerrenoService`, `TerrenoDAO`, `MenuTerrenos` |
| RF03 | CRUD + finalizar plantio | `Plantio`, `PlantioService`, `PlantioDAO`, `MenuPlantios` |
| RF04 | CRUD completo | `Muda`, `MudaService`, `MudaDAO`, `MenuMudas` |
| RF05 | CRUD de pragas, doenças e chuva, com alertas automáticos | `RegistroMonitoramento` e subclasses, `MonitoramentoService`, `MonitoramentoDAO`, `MenuMonitoramento` |
| RF06 | CRUD completo, com saldo disponível para venda | `Colheita`, `ColheitaService`, `ColheitaDAO`, `MenuColheitas` |
| RF07 | CRUD completo, com controle da quantidade disponível | `Venda`, `VendaService`, `VendaDAO`, `MenuVendas` |
| RF08 | Avisos automáticos e manuais, concluir, reabrir, editar e excluir | `Aviso`, `AvisoService`, `AvisoDAO`, `MenuAvisos` |

## Pilares de POO no código

| Pilar | Onde está |
| ----- | --------- |
| **Classe abstrata** | `model.Usuario`, `model.RegistroMonitoramento`, `dao.AbstractDAO<T>`, `view.Menu`, `view.MenuProdutorBase` |
| **Interface** | `dao.GenericDAO<T>`, contrato de CRUD implementado por todos os DAOs |
| **Herança** | `Produtor` e `Administrador` herdam de `Usuario`. `RegistroPraga`, `RegistroDoenca` e `RegistroClima` herdam de `RegistroMonitoramento`. Cada DAO herda de `AbstractDAO`. Cada tela herda de `Menu`. |
| **Polimorfismo (`@Override`)** | `autenticar()` tem regra própria em `Produtor` e `Administrador` (o administrador exige senha de no mínimo 8 caracteres). `gerarAlerta()` tem regra própria em cada tipo de monitoramento e cria avisos automaticamente. `mapear()`, `inserir()` e `atualizar()` são sobrescritos em cada DAO. `getOpcoes()` e `executarOpcao()` são sobrescritos em cada menu. |
| **Composição 1:N** | `Produtor` → `Terreno`s → `Plantio`s → `Muda`s, `RegistroMonitoramento`s e `Colheita`s → `Venda`s. É montada em `TerrenoService.consultarHistorico()` e exibida em "Histórico completo de um terreno". |
| **Encapsulamento** | Atributos `private` com getters e setters. A senha é guardada só como hash SHA-256. |

## Banco de dados

O `database/schema.sql` segue o DER da 1ª entrega:

* `usuario` guarda Produtor e Administrador. A coluna `tipo_usuario` indica qual subclasse o `UsuarioDAO` deve criar.
* `registro_monitoramento` guarda os dados comuns. `registro_praga`, `registro_doenca` e `registro_clima` guardam os dados de cada subclasse, com a mesma chave primária. O `MonitoramentoDAO` grava as duas tabelas na mesma transação.
* As chaves estrangeiras usam `ON DELETE CASCADE`. Ao excluir um produtor, todos os dados dele são removidos.

## Cronograma de Execução (1ª Entrega — 26/08/2026 a 11/09/2026)

| Data       | Atividade                                               | Responsável          |
| ---------- | ------------------------------------------------------- | -------------------- |
| 26/08/2026 | Levantamento de requisitos e definição do escopo        | Todos os integrantes |
| 01/09/2026 | Modelagem do Diagrama de Classe e do DER                | Matheus              |
| 02/09/2026 | Criação e estruturação do repositório no GitHub         | João                 |
| 03/09/2026 | Redação da Justificativa Técnica e Concepção do projeto | Luiz                 |
| 09/09/2026 | Revisão geral do documento e formatação do PDF          | Todos os integrantes |
| 11/09/2026 | Entrega final da 1ª etapa (PDF + link do GitHub)        | Todos os integrantes |

## Cronograma de Execução (2ª Entrega)

| Data       | Atividade                                                                  | Responsável          |
| ---------- | -------------------------------------------------------------------------- | -------------------- |
| 12/11/2026 | Configuração do Maven, conexão JDBC e utilitários (`/src/util`)            | João                 |
| 13/11/2026 | Ajuste do `schema.sql` ao DER e classes de domínio (`/src/model`)          | Matheus              |
| 16/11/2026 | Camada de persistência — DAOs com CRUD (`/src/dao`)                        | João                 |
| 18/11/2026 | Regras de negócio e validações (`/src/service`)                            | Luiz                 |
| 20/11/2026 | Menus do terminal e ponto de entrada (`/src/view`, `/src/app`)             | Luiz                 |
| 23/11/2026 | Testes integrados com o MySQL e atualização do README                      | Matheus              |
| 25/11/2026 | Revisão final e entrega (link do GitHub)                                   | Todos os integrantes |

## Arquitetura e Tecnologias

* **Linguagem:** Java 17
* **Banco de Dados:** MySQL 8+
* **Conexão:** JDBC (driver `mysql-connector-j`)
* **Build:** Maven
* **Padrão Arquitetural:** Arquitetura em camadas (View → Service → DAO → Model)

Mais detalhes e a justificativa completa dessas escolhas estão no documento da 1ª entrega, em `/Docs`.
