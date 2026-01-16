# 🚀 Guia de Setup - SAP ERP System

## Pré-requisitos

- Java 17 ou superior
- Node.js 18+ e npm
- Docker e Docker Compose
- Maven 3.9+ (opcional, se não usar Docker)

## Opção 1: Setup Completo com Docker (Recomendado)

### 1. Clone e entre no diretório
```bash
cd C:\Sap
```

### 2. Configure variáveis de ambiente (opcional)
Crie um arquivo `.env` na raiz:
```env
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-256-bit-secret-key-change-in-production-minimum-32-characters
```

### 3. Inicie os serviços
```bash
docker-compose up -d
```

Isso irá iniciar:
- PostgreSQL (porta 5432)
- Redis (porta 6379)
- Backend Spring Boot (porta 8080)
- Frontend Angular (porta 4200)

### 4. Aguarde a inicialização
Aguarde alguns segundos para o backend compilar e iniciar. Verifique os logs:
```bash
docker-compose logs -f backend
```

### 5. Acesse o sistema
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- Swagger: http://localhost:8080/api/swagger-ui.html

## Opção 2: Setup Manual (Sem Docker)

### Backend

1. **Configure o banco de dados**
```bash
# Inicie PostgreSQL e Redis via Docker
docker-compose up postgres redis -d
```

2. **Configure o application.yml**
Edite `backend/src/main/resources/application.yml` se necessário.

3. **Execute o backend**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend

1. **Instale dependências**
```bash
cd frontend
npm install
```

2. **Inicie o frontend**
```bash
npm start
```

## 🔐 Primeiro Acesso

### Credenciais Padrão (Desenvolvimento)

O sistema cria automaticamente um usuário admin quando iniciado pela primeira vez:

- **Email**: `admin@demo.com`
- **Senha**: `admin123`

⚠️ **IMPORTANTE**: Altere a senha após o primeiro login!

## 📝 Criando Dados Iniciais

### Via API (Recomendado)

1. **Criar Empresa**
```bash
POST http://localhost:8080/api/base/companies
Content-Type: application/json

{
  "name": "Minha Empresa",
  "tradeName": "Empresa Ltda",
  "cnpj": "12345678000190",
  "email": "contato@empresa.com"
}
```

2. **Criar Usuário**
```bash
POST http://localhost:8080/api/base/users
Authorization: Bearer {seu-token}
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@empresa.com",
  "password": "senha123",
  "companyId": "{id-da-empresa}"
}
```

## 🛠️ Comandos Úteis

### Docker
```bash
# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down

# Parar e remover volumes (limpar dados)
docker-compose down -v

# Reconstruir imagens
docker-compose build --no-cache
```

### Backend
```bash
# Compilar
mvn clean package

# Executar testes
mvn test

# Verificar dependências
mvn dependency:tree
```

### Frontend
```bash
# Instalar dependências
npm install

# Build produção
npm run build

# Executar testes
npm test
```

## 🔧 Troubleshooting

### Backend não inicia
- Verifique se PostgreSQL está rodando
- Verifique as portas 8080 e 5432
- Veja os logs: `docker-compose logs backend`

### Frontend não conecta ao backend
- Verifique se o backend está rodando
- Verifique `frontend/src/environments/environment.ts`
- Verifique CORS no backend

### Erro de conexão com banco
- Verifique se PostgreSQL está rodando: `docker-compose ps`
- Verifique credenciais no `application.yml`
- Verifique se o banco `sap_erp` foi criado

### Erro de autenticação
- Verifique se o JWT_SECRET está configurado
- Limpe o cache do navegador
- Faça logout e login novamente

## 📚 Próximos Passos

1. Explore a API via Swagger
2. Crie sua primeira empresa
3. Configure permissões e roles
4. Comece a usar o módulo Financeiro
5. Explore os outros módulos conforme desenvolvidos

## 🆘 Suporte

Para problemas ou dúvidas:
1. Verifique os logs: `docker-compose logs`
2. Consulte a documentação da API: http://localhost:8080/api/swagger-ui.html
3. Verifique o README.md principal

---

**Boa sorte com seu ERP! 🚀**
