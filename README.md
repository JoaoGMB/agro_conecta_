# AgroConecta — Sistema de Gestão e Conexão para Micro Produtores Rurais

Atividade de Estudo Programada (AEP) — 4º Semestre (2026.2) Engenharia de Software | Análise e Desenvolvimento de Sistemas — UniCesumar

**ODS Vinculadas:** ODS 2 — Fome Zero e Agricultura Sustentável; ODS 9 — Indústria, Inovação e Infraestrutura; ODS 12 — Consumo e Produção Responsáveis

**Equipe de Desenvolvimento:**

* João Guilherme Muller Bordin
* Matheus José de Azevedo
* Luiz Otávio Severino

## Sobre o projeto

O AgroConecta é um sistema de gestão agrícola voltado para micro produtores rurais. O sistema permite organizar o ciclo produtivo, desde o cadastro do produtor e terreno até o plantio, monitoramento, colheita e venda da produção.

O documento completo da 1ª entrega está em `/docs`.

## Estrutura do repositório

```text
/src        → código-fonte Java (pacotes model, dao, service e util)
/docs       → documento da entrega e diagramas
/database   → script SQL de criação das tabelas (schema.sql)
/.gitignore → arquivos e diretórios que não devem ser versionados
```

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

## Cronograma de Execução (1ª Entrega — 26/08/2026 a 11/09/2026)

| Data       | Atividade                                               | Responsável          |
| ---------- | ------------------------------------------------------- | -------------------- |
| 26/08/2026 | Levantamento de requisitos e definição do escopo        | Todos os integrantes |
| 01/09/2026 | Modelagem do Diagrama de Classe e do DER                | Matheus              |
| 02/09/2026 | Criação e estruturação do repositório no GitHub         | João                 |
| 03/09/2026 | Redação da Justificativa Técnica e Concepção do projeto | Luiz                 |
| 09/09/2026 | Revisão geral do documento e formatação do PDF          | Todos os integrantes |
| 11/09/2026 | Entrega final da 1ª etapa (PDF + link do GitHub)        | Todos os integrantes |

## Arquitetura e Tecnologias

* **Linguagem:** Java
* **Banco de Dados:** MySQL
* **Conexão:** JDBC
* **Padrão Arquitetural:** Arquitetura em camadas (Model → DAO/Repository → Service)

Mais detalhes e a justificativa completa dessas escolhas estão no documento da 1ª entrega, em `/docs`.
