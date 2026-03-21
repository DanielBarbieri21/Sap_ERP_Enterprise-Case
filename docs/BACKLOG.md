# Backlog Tecnico

## Plataforma

- Consolidar migrations versionadas para toda a base
- Adicionar cobertura de testes por camada
- Padronizar DTOs, mappers e contratos de erro
- Instrumentar observabilidade com dashboards e tracing
- Migrar schema principal do JPA auto-ddl para migrations completas do Flyway
- Adicionar seed demo mais rica para MM, SD, WM e HCM

## Modulo Base

- CRUD completo de usuarios, empresas, perfis e permissoes
- Gestao de senha, bloqueio, reset e auditoria de acesso
- Refinar RBAC com autorizacao por recurso

## Modulo FI

- Fechamento financeiro por periodo
- Contas a pagar e receber com workflow
- Centro de custo e consolidacao por tenant

## Modulos em Evolucao

- `CO`: documentos contabeis, partidas dobradas, DRE
- `MM`: requisicoes, pedidos, recebimento e aprovacoes
- `SD`: pedidos, faturamento e emissao de nota
- `WM`: estoque, movimentacao, inventario e estoque minimo
- `HCM`: funcionarios, folha e ponto

## Integracoes

- PIX com webhook e conciliacao
- Boletos com remessa/retorno
- NFe com emissao, consulta e armazenamento de XML
- Marketplaces e catalogos externos

## Frontend

- Layout enterprise com shell reutilizavel
- KPIs, graficos e filtros por periodo
- Estados de loading, empty e error em todos os modulos
- Design system consistente e responsivo
