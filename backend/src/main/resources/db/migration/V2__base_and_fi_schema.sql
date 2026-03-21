CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    name VARCHAR(255) NOT NULL,
    trade_name VARCHAR(255),
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    ie VARCHAR(20),
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    country VARCHAR(100),
    logo_url VARCHAR(500),
    settings TEXT
);

CREATE INDEX IF NOT EXISTS idx_company_cnpj ON companies (cnpj);
CREATE INDEX IF NOT EXISTS idx_company_active ON companies (active);

CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    module VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_permission_code ON permissions (code);

CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE INDEX IF NOT EXISTS idx_role_name ON roles (name);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(500),
    company_id UUID NOT NULL,
    last_login TIMESTAMP,
    must_change_password BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX IF NOT EXISTS idx_user_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_user_tenant ON users (tenant_id);
CREATE INDEX IF NOT EXISTS idx_user_active ON users (active);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS chart_of_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    nature VARCHAR(20) NOT NULL,
    parent_id UUID,
    level INTEGER NOT NULL,
    balance NUMERIC(19,2) DEFAULT 0,
    CONSTRAINT fk_chart_of_accounts_parent FOREIGN KEY (parent_id) REFERENCES chart_of_accounts (id)
);

CREATE INDEX IF NOT EXISTS idx_chart_account_code ON chart_of_accounts (code);
CREATE INDEX IF NOT EXISTS idx_chart_account_tenant ON chart_of_accounts (tenant_id);

CREATE TABLE IF NOT EXISTS financial_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    document_number VARCHAR(50),
    transaction_date DATE NOT NULL,
    due_date DATE,
    payment_date DATE,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    account_id UUID NOT NULL,
    description VARCHAR(500) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    paid_amount NUMERIC(19,2) DEFAULT 0,
    installment_number INTEGER,
    total_installments INTEGER,
    payment_method VARCHAR(50),
    notes TEXT,
    CONSTRAINT fk_financial_transactions_account FOREIGN KEY (account_id) REFERENCES chart_of_accounts (id)
);

CREATE INDEX IF NOT EXISTS idx_transaction_date ON financial_transactions (transaction_date);
CREATE INDEX IF NOT EXISTS idx_transaction_tenant ON financial_transactions (tenant_id);
CREATE INDEX IF NOT EXISTS idx_transaction_type ON financial_transactions (type);
