# AgroConecta — Sistema de Gestão e Conexão para Micro Produtores Rurais

Atividade de Estudo Programada (AEP) — 4º Semestre (2026.2) Engenharia de Software | Análise e Desenvolvimento de Sistemas — UniCesumar

**ODS Vinculadas:** ODS 2 — Fome Zero e Agricultura Sustentável; ODS 9 — Indústria, Inovação e Infraestrutura; ODS 12 — Consumo e Produção Responsáveis

**Equipe de Desenvolvimento:**

* João Guilherme Muller Bordin
* Matheus José de Azevedo
* Luiz Otávio Severino

## Sobre o projeto

O AgroConecta é um sistema de gestão agrícola voltado para micro produtores rurais. Ele organiza o ciclo produtivo, desde o cadastro do produtor e do terreno até o plantio, o monitoramento, a colheita e a venda da produção.

O documento da 1ª entrega e os diagramas (classes e DER) estão em `/Docs`.

## Estrutura do repositório

```text
/src        → código-fonte Java (app, model, dao, service, util e view)
/Docs       → documento da 1ª entrega, Diagrama de Classes e DER
/database   → script SQL de criação do banco (schema.sql)
/config     → db.properties (configuração opcional da conexão)
pom.xml     → build Maven (Java 17 + driver MySQL)
```

## Como executar

**Pré-requisitos:** JDK 17 ou superior, Maven e MySQL 8 ou superior.

1. **Criar o banco.** No MySQL Workbench, conecte com o usuário `root`, abra o arquivo `database/schema.sql` e execute com o raio ⚡. O script cria o banco `agroconecta`, o usuário do sistema, as tabelas e o administrador padrão.
2. **Abrir o projeto.** No IntelliJ IDEA, abra a pasta que contém o `pom.xml` e aguarde o Maven baixar as dependências.
3. **Executar.** Rode a classe `AgroConectaApp` (`src/app/AgroConectaApp.java`).

Pelo terminal, na pasta do projeto: `mvn compile exec:java`.

Nenhuma configuração é necessária. O sistema já se conecta ao MySQL em `localhost:3306` com o usuário `agroconecta`, criado pelo `schema.sql`. Se o MySQL usar outra porta, ajuste o `config/db.properties`.

### Primeiro acesso

| Perfil | CPF | Senha |
| ------ | --- | ----- |
| Administrador | `000.000.000-00` | `admin123` |

O produtor se cadastra pela opção **"Sou produtor e quero me cadastrar"** da tela inicial, informando um CPF válido e uma senha de pelo menos 4 caracteres.

## Requisitos e onde foram implementados

| Requisito | Descrição | Implementação |
| --------- | --------- | ------------- |
| RF01 | Cadastro de produtores rurais, com nome completo, CPF, telefone, e-mail e endereço. | `Produtor`, `ProdutorService`, `UsuarioDAO`, `MenuAdministrador`, `MenuMeusDados` |
| RF02 | Cadastro de terrenos vinculados a um produtor. | `Terreno`, `TerrenoService`, `TerrenoDAO`, `MenuTerrenos` |
| RF03 | Registro de um plantio para um terreno. | `Plantio`, `PlantioService`, `PlantioDAO`, `MenuPlantios` |
| RF04 | Registro das mudas utilizadas em um plantio. | `Muda`, `MudaService`, `MudaDAO`, `MenuMudas` |
| RF05 | Registro de ocorrências de pragas, doenças e volume de chuva. | `RegistroMonitoramento`, `RegistroPraga`, `RegistroDoenca`, `RegistroClima`, `MonitoramentoService`, `MonitoramentoDAO`, `MenuMonitoramento` |
| RF06 | Registro da colheita de um plantio. | `Colheita`, `ColheitaService`, `ColheitaDAO`, `MenuColheitas` |
| RF07 | Cadastro de vendas vinculadas a uma colheita. | `Venda`, `VendaService`, `VendaDAO`, `MenuVendas` |
| RF08 | Tela de avisos com pendências para o produtor. | `Aviso`, `AvisoService`, `AvisoDAO`, `MenuAvisos` |

**Requisitos não funcionais:** usabilidade, simplicidade, segurança, desempenho e manutenibilidade.

Todos os dados são gravados no MySQL via JDBC (CRUD completo em cada requisito).

## Pilares de POO no código

| Pilar | Onde está |
| ----- | --------- |
| **Classe abstrata** | `model.Usuario`, `model.RegistroMonitoramento`, `dao.AbstractDAO<T>`, `view.Menu`, `view.MenuProdutorBase` |
| **Interface** | `dao.GenericDAO<T>`, contrato de CRUD implementado por todos os DAOs |
| **Herança** | `Produtor` e `Administrador` herdam de `Usuario`. `RegistroPraga`, `RegistroDoenca` e `RegistroClima` herdam de `RegistroMonitoramento`. Cada DAO herda de `AbstractDAO`. Cada tela herda de `Menu`. |
| **Polimorfismo (`@Override`)** | `autenticar()` tem regra própria em `Produtor` e `Administrador` (o administrador exige senha de no mínimo 8 caracteres). `gerarAlerta()` tem regra própria em cada tipo de monitoramento e cria avisos automaticamente. `mapear()`, `inserir()` e `atualizar()` são sobrescritos em cada DAO. `getOpcoes()` e `executarOpcao()` são sobrescritos em cada menu. |
| **Composição 1:N** | `Produtor` → `Terreno`s → `Plantio`s → `Muda`s, `RegistroMonitoramento`s e `Colheita`s → `Venda`s. É montada em `TerrenoService.consultarHistorico()` e exibida em "Histórico completo de um terreno". |
| **Encapsulamento** | Atributos `private` com getters e setters. A senha é guardada só como hash SHA-256. |

## Cronograma de Execução

### 1ª Entrega (26/08/2026 a 11/09/2026)

| Data       | Atividade                                               | Responsável          |
| ---------- | ------------------------------------------------------- | -------------------- |
| 26/08/2026 | Levantamento de requisitos e definição do escopo        | Todos os integrantes |
| 01/09/2026 | Modelagem do Diagrama de Classe e do DER                | Matheus              |
| 02/09/2026 | Criação e estruturação do repositório no GitHub         | João                 |
| 03/09/2026 | Redação da Justificativa Técnica e Concepção do projeto | Luiz                 |
| 09/09/2026 | Revisão geral do documento e formatação do PDF          | Todos os integrantes |
| 11/09/2026 | Entrega final da 1ª etapa (PDF + link do GitHub)        | Todos os integrantes |

### 2ª Entrega (até 12/11/2026)

| Data       | Atividade                                                       | Responsável          |
| ---------- | --------------------------------------------------------------- | -------------------- |
| 14/10/2026 | Configuração do Maven, conexão JDBC e utilitários (`/src/util`) | João                 |
| 19/10/2026 | Ajuste do `schema.sql` ao DER e classes de domínio (`/src/model`) | Matheus            |
| 26/10/2026 | Camada de persistência: DAOs com CRUD (`/src/dao`)              | João                 |
| 02/11/2026 | Regras de negócio e validações (`/src/service`)                 | Luiz                 |
| 09/11/2026 | Menus do terminal e ponto de entrada (`/src/view`, `/src/app`)  | Luiz                 |
| 11/11/2026 | Testes integrados com o MySQL e atualização do README           | Matheus              |
| 12/11/2026 | Revisão final e entrega (link do GitHub)                        | Todos os integrantes |

## Arquitetura e Tecnologias

* **Linguagem:** Java 17
* **Banco de Dados:** MySQL 8+
* **Conexão:** JDBC (driver `mysql-connector-j`)
* **Build:** Maven
* **Padrão arquitetural:** arquitetura em camadas (View → Service → DAO → Model)

A justificativa completa dessas escolhas está no documento da 1ª entrega, em `/Docs`.
