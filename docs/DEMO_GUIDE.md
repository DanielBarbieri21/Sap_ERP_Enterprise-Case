# Demo Guide

## Objetivo

Apresentar o projeto como um case enterprise full stack com narrativa clara para recrutadores, liderancas tecnicas e clientes.

## Roteiro de demonstracao

1. Login no sistema com `admin@demo.com`
2. Dashboard executivo com KPIs financeiros e operacionais
3. Navegacao lateral entre FI, MM, SD, WM e HCM
4. Criacao de um lancamento financeiro
5. Consulta de estoque e produtos ativos
6. Exibicao dos modulos de compras, vendas e RH
7. Swagger UI e CI no GitHub como prova de engenharia

## Capturas recomendadas

- `docs/assets/dashboard-overview.png`
- `docs/assets/financial-module.png`
- `docs/assets/warehouse-module.png`
- `docs/assets/login-screen.png`
- `docs/assets/app-walkthrough.gif`

## Narrativa sugerida

- arquitetura modular por dominio
- autenticacao JWT e multi-tenancy
- Flyway para governanca de schema
- contratos DTO para APIs mais estaveis
- Angular com shell enterprise e navegacao unificada

## Observacao

Se o ambiente local nao permitir build ou execucao imediata, use este roteiro como base e capture as telas assim que `mvn` e `npm` estiverem funcionais.
