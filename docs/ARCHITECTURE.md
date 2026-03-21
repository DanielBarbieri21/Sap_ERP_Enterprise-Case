# Arquitetura

## Objetivo

Apresentar um ERP full stack com cara de produto enterprise, cobrindo autenticacao, modulos de negocio, multi-tenancy e operacao local.

## Camadas

### Backend

- `core/`: seguranca, auditoria, configuracao, mensageria e servicos compartilhados
- `modules/`: dominios do ERP separados por contexto de negocio
- `repository`: acesso a dados
- `service`: regras de negocio
- `controller`: contratos HTTP

### Frontend

- `auth/`: autenticacao
- `core/`: guards, interceptors e servicos base
- `dashboard/`: shell e KPIs principais
- `modules/`: areas funcionais do ERP

## Principios

- Separacao clara por dominio
- Configuracao por ambiente
- Seguranca stateless
- Evolucao incremental para cloud e Kubernetes
- Legibilidade de portfolio acima de complexidade desnecessaria

## Fluxo de autenticacao

1. Usuario autentica no endpoint `/auth/login`
2. Backend gera JWT com contexto de tenant
3. Frontend persiste token e injeta no interceptor HTTP
4. Filtro JWT restaura autenticacao e tenant a cada request

## Operacao

- `docker-compose.yml` para bootstrap local
- `k8s/` para demonstracao de deploy containerizado
- `Actuator` para health, metrics e Prometheus
- `Flyway` para versionamento gradual do schema, com inicio pelos modulos `base` e `fi`
