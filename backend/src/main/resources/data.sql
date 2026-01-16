-- Script de inicialização de dados base
-- Este script será executado apenas se hibernate.ddl-auto estiver configurado para create ou create-drop

-- Inserir empresa padrão (apenas exemplo - em produção, criar via API)
-- INSERT INTO companies (id, name, trade_name, cnpj, email, active, created_at, updated_at, version)
-- VALUES (gen_random_uuid(), 'Empresa Demo', 'Demo Ltda', '12345678000190', 'contato@demo.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Inserir permissões base
-- INSERT INTO permissions (id, code, name, description, module, active, created_at, updated_at, version)
-- VALUES 
--     (gen_random_uuid(), 'USER_CREATE', 'Criar Usuário', 'Permissão para criar usuários', 'BASE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
--     (gen_random_uuid(), 'USER_UPDATE', 'Atualizar Usuário', 'Permissão para atualizar usuários', 'BASE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
--     (gen_random_uuid(), 'USER_DELETE', 'Deletar Usuário', 'Permissão para deletar usuários', 'BASE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
--     (gen_random_uuid(), 'FI_VIEW', 'Visualizar Financeiro', 'Permissão para visualizar módulo financeiro', 'FI', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
--     (gen_random_uuid(), 'FI_CREATE', 'Criar Transação', 'Permissão para criar transações financeiras', 'FI', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
--     (gen_random_uuid(), 'FI_PAY', 'Pagar Transação', 'Permissão para pagar transações', 'FI', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Nota: Em produção, use scripts de migração (Flyway/Liquibase) ao invés de data.sql
