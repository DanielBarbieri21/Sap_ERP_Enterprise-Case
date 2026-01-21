# SAP ERP System

Sistema ERP modular profissional inspirado no SAP, construído com arquitetura moderna e escalável.

## 🏗️ Arquitetura

### Backend
- **Java 17** + **Spring Boot 3.2**
- **PostgreSQL** (banco principal)
- **Redis** (cache)
- **JWT** (autenticação)
- **Multi-tenant** (suporte a múltiplas empresas)

### Frontend
- **Angular 17** (standalone components)
- **TypeScript**
- **Material Design**

### Infraestrutura
- **Docker** + **Docker Compose**
- **Maven** (build)

## 📦 Módulos Implementados

### ✅ Módulo Base
- Usuários
- Empresas
- Perfis e Permissões (RBAC)
- Autenticação JWT

### ✅ Módulo Financeiro (FI)
- Plano de Contas
- Transações Financeiras
- Contas a Pagar/Receber
- Centro de Custos
- Fluxo de Caixa

### 🚧 Em Desenvolvimento
- Módulo Contábil (CO)
- Módulo de Compras (MM)
- Módulo de Vendas (SD)
- Módulo de Estoque (WM)
- Módulo de RH (HCM)

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Node.js 18+
- Docker e Docker Compose
- Maven 3.9+

### Opção 1: Docker Compose (Recomendado)

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down
```

### Opção 2: Execução Local

#### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm start
```

#### Banco de Dados
```bash
docker-compose up postgres redis -d
```

## 🔐 Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-256-bit-secret-key-change-in-production-minimum-32-characters
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Primeiro Acesso

1. Acesse `http://localhost:4200`
2. Faça login (criar usuário inicial via API ou script de inicialização)

## 📚 API Documentation

Após iniciar o backend, acesse:
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- API Docs: `http://localhost:8080/api/api-docs`

## 🧪 Testes

```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

## 📁 Estrutura do Projeto

```
.
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/sap/erp/
│   │   │   │   ├── core/           # Core do sistema
│   │   │   │   ├── modules/
│   │   │   │   │   ├── base/       # Módulo base
│   │   │   │   │   ├── fi/         # Módulo Financeiro
│   │   │   │   │   ├── co/         # Módulo Contábil (futuro)
│   │   │   │   │   └── ...
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/              # Serviços core
│   │   │   ├── auth/              # Autenticação
│   │   │   ├── dashboard/         # Dashboard
│   │   │   └── modules/           # Módulos do ERP
│   │   └── environments/
│   └── package.json
├── docker-compose.yml
└── README.md
```

## 🔒 Segurança

- Autenticação JWT
- Spring Security
- RBAC (Role-Based Access Control)
- Multi-tenant isolado
- Criptografia de senhas (BCrypt)
- Validação de entrada
- Soft delete

## 📈 Próximos Passos

1. Implementar módulos restantes (CO, MM, SD, WM, HCM)
2. Adicionar testes automatizados
3. Implementar auditoria completa
4. Integração com APIs externas (PIX, boletos, NFe)
5. Dashboard com gráficos e KPIs
6. Relatórios (Jasper Reports)
7. Mensageria (Kafka/RabbitMQ)
8. Deploy em Kubernetes

## ☸️ Kubernetes

Manifests iniciais adicionados em [k8s/](k8s/):
- [k8s/namespace.yaml](k8s/namespace.yaml)
- [k8s/backend-deployment.yaml](k8s/backend-deployment.yaml)
- [k8s/frontend-deployment.yaml](k8s/frontend-deployment.yaml)
- [k8s/postgres-statefulset.yaml](k8s/postgres-statefulset.yaml)
- [k8s/redis-deployment.yaml](k8s/redis-deployment.yaml)
- [k8s/rabbitmq-deployment.yaml](k8s/rabbitmq-deployment.yaml)
- [k8s/secrets.yaml](k8s/secrets.yaml)

Passos rápidos:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/postgres-statefulset.yaml
kubectl apply -f k8s/redis-deployment.yaml
kubectl apply -f k8s/rabbitmq-deployment.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
```

Atenção: atualize a imagem do backend/frontend nos manifests (`REPLACE_WITH_REGISTRY/...`).

## 🔎 Auditoria

Aspecto de auditoria criado em [backend/src/main/java/br/com/sap/erp/core/audit/AuditAspect.java](backend/src/main/java/br/com/sap/erp/core/audit/AuditAspect.java).

Anote métodos com `@Auditable("ACTION")` para registrar auditoria automaticamente.

## 🧪 Testes

Teste unitário do JWT em [backend/src/test/java/br/com/sap/erp/core/service/JwtServiceTest.java](backend/src/test/java/br/com/sap/erp/core/service/JwtServiceTest.java).


## 🤝 Contribuindo

Este é um projeto educacional/profissional. Sinta-se livre para adaptar conforme suas necessidades.


   ---

🛠️ **Software desenvolvido por Daniel Barbieri**  
Engenheiro de Software | Full Stack Developer  

Código construído com foco em eficiência, organização, escalabilidade e boas práticas de desenvolvimento.

🌐 GitHub: https://github.com/DanielBarbieri21  
💼 LinkedIn: https://www.linkedin.com/in/daniel-barbieri-4990462a/

---



