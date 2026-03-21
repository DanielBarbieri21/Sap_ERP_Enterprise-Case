CREATE TABLE IF NOT EXISTS cost_centers (
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
    parent_id UUID,
    level INTEGER NOT NULL,
    CONSTRAINT fk_cost_centers_parent FOREIGN KEY (parent_id) REFERENCES cost_centers (id)
);

CREATE INDEX IF NOT EXISTS idx_cost_center_code ON cost_centers (code);
CREATE INDEX IF NOT EXISTS idx_cost_center_tenant ON cost_centers (tenant_id);

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    unit VARCHAR(10),
    type VARCHAR(20),
    cost_price NUMERIC(19,2),
    sale_price NUMERIC(19,2),
    min_stock NUMERIC(19,3),
    max_stock NUMERIC(19,3),
    current_stock NUMERIC(19,3) DEFAULT 0,
    requires_batch_control BOOLEAN DEFAULT FALSE,
    requires_expiry_control BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_product_code ON products (code);
CREATE INDEX IF NOT EXISTS idx_product_tenant ON products (tenant_id);

CREATE TABLE IF NOT EXISTS stock_movements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    product_id UUID NOT NULL,
    movement_date DATE NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_cost NUMERIC(19,2),
    total_cost NUMERIC(19,2),
    document_type VARCHAR(50),
    document_id UUID,
    document_number VARCHAR(50),
    batch_number VARCHAR(50),
    expiry_date DATE,
    location VARCHAR(100),
    notes VARCHAR(500),
    CONSTRAINT fk_stock_movements_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE INDEX IF NOT EXISTS idx_movement_date ON stock_movements (movement_date);
CREATE INDEX IF NOT EXISTS idx_movement_tenant ON stock_movements (tenant_id);
CREATE INDEX IF NOT EXISTS idx_movement_product ON stock_movements (product_id);

CREATE TABLE IF NOT EXISTS inventories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    inventory_number VARCHAR(50) NOT NULL UNIQUE,
    inventory_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
);

CREATE INDEX IF NOT EXISTS idx_inv_number ON inventories (inventory_number);
CREATE INDEX IF NOT EXISTS idx_inv_tenant ON inventories (tenant_id);
CREATE INDEX IF NOT EXISTS idx_inv_date ON inventories (inventory_date);

CREATE TABLE IF NOT EXISTS inventory_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    inventory_id UUID NOT NULL,
    product_id UUID NOT NULL,
    booked_quantity NUMERIC(19,3),
    counted_quantity NUMERIC(19,3),
    difference NUMERIC(19,3),
    location VARCHAR(100),
    CONSTRAINT fk_inventory_items_inventory FOREIGN KEY (inventory_id) REFERENCES inventories (id),
    CONSTRAINT fk_inventory_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE IF NOT EXISTS purchase_requisitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    requisition_number VARCHAR(50) NOT NULL UNIQUE,
    requisition_date DATE NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_amount NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_req_number ON purchase_requisitions (requisition_number);
CREATE INDEX IF NOT EXISTS idx_req_tenant ON purchase_requisitions (tenant_id);
CREATE INDEX IF NOT EXISTS idx_req_date ON purchase_requisitions (requisition_date);

CREATE TABLE IF NOT EXISTS purchase_requisition_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    requisition_id UUID NOT NULL,
    product_code VARCHAR(50),
    product_description VARCHAR(500) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_price NUMERIC(19,2),
    total_amount NUMERIC(19,2) DEFAULT 0,
    unit VARCHAR(10),
    CONSTRAINT fk_purchase_requisition_items_req FOREIGN KEY (requisition_id) REFERENCES purchase_requisitions (id)
);

CREATE TABLE IF NOT EXISTS purchase_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    order_date DATE NOT NULL,
    expected_delivery_date DATE,
    supplier_id UUID,
    supplier_name VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_amount NUMERIC(19,2) DEFAULT 0,
    discount_amount NUMERIC(19,2) DEFAULT 0,
    tax_amount NUMERIC(19,2) DEFAULT 0,
    final_amount NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_po_number ON purchase_orders (order_number);
CREATE INDEX IF NOT EXISTS idx_po_tenant ON purchase_orders (tenant_id);
CREATE INDEX IF NOT EXISTS idx_po_supplier ON purchase_orders (supplier_id);

CREATE TABLE IF NOT EXISTS purchase_order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    order_id UUID NOT NULL,
    product_code VARCHAR(50),
    product_description VARCHAR(500) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_price NUMERIC(19,2),
    total_amount NUMERIC(19,2) DEFAULT 0,
    received_quantity NUMERIC(19,3) DEFAULT 0,
    unit VARCHAR(10),
    CONSTRAINT fk_purchase_order_items_order FOREIGN KEY (order_id) REFERENCES purchase_orders (id)
);

CREATE TABLE IF NOT EXISTS goods_receipts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    receipt_date DATE NOT NULL,
    purchase_order_id UUID,
    supplier_id UUID,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_amount NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_gr_number ON goods_receipts (receipt_number);
CREATE INDEX IF NOT EXISTS idx_gr_tenant ON goods_receipts (tenant_id);
CREATE INDEX IF NOT EXISTS idx_gr_order ON goods_receipts (purchase_order_id);

CREATE TABLE IF NOT EXISTS goods_receipt_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    receipt_id UUID NOT NULL,
    product_code VARCHAR(50),
    product_description VARCHAR(500) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_price NUMERIC(19,2),
    total_amount NUMERIC(19,2) DEFAULT 0,
    batch_number VARCHAR(50),
    expiry_date DATE,
    unit VARCHAR(10),
    CONSTRAINT fk_goods_receipt_items_receipt FOREIGN KEY (receipt_id) REFERENCES goods_receipts (id)
);

CREATE TABLE IF NOT EXISTS sales_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    order_date DATE NOT NULL,
    customer_id UUID,
    customer_name VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_amount NUMERIC(19,2) DEFAULT 0,
    discount_amount NUMERIC(19,2) DEFAULT 0,
    tax_amount NUMERIC(19,2) DEFAULT 0,
    final_amount NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_so_number ON sales_orders (order_number);
CREATE INDEX IF NOT EXISTS idx_so_tenant ON sales_orders (tenant_id);
CREATE INDEX IF NOT EXISTS idx_so_customer ON sales_orders (customer_id);

CREATE TABLE IF NOT EXISTS sales_order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    order_id UUID NOT NULL,
    product_code VARCHAR(50),
    product_description VARCHAR(500) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_price NUMERIC(19,2),
    total_amount NUMERIC(19,2) DEFAULT 0,
    unit VARCHAR(10),
    CONSTRAINT fk_sales_order_items_order FOREIGN KEY (order_id) REFERENCES sales_orders (id)
);

CREATE TABLE IF NOT EXISTS invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    invoice_date DATE NOT NULL,
    customer_id UUID,
    customer_name VARCHAR(255),
    sales_order_id UUID,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_amount NUMERIC(19,2) DEFAULT 0,
    tax_amount NUMERIC(19,2) DEFAULT 0,
    final_amount NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_invoice_number ON invoices (invoice_number);
CREATE INDEX IF NOT EXISTS idx_invoice_tenant ON invoices (tenant_id);
CREATE INDEX IF NOT EXISTS idx_invoice_customer ON invoices (customer_id);

CREATE TABLE IF NOT EXISTS invoice_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    invoice_id UUID NOT NULL,
    product_code VARCHAR(50),
    product_description VARCHAR(500) NOT NULL,
    quantity NUMERIC(19,3) NOT NULL,
    unit_price NUMERIC(19,2),
    total_amount NUMERIC(19,2) DEFAULT 0,
    unit VARCHAR(10),
    CONSTRAINT fk_invoice_items_invoice FOREIGN KEY (invoice_id) REFERENCES invoices (id)
);

CREATE TABLE IF NOT EXISTS employees (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    rg VARCHAR(20),
    birth_date DATE,
    hire_date DATE,
    termination_date DATE,
    position VARCHAR(100),
    department VARCHAR(100),
    salary NUMERIC(19,2),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(500)
);

CREATE INDEX IF NOT EXISTS idx_emp_code ON employees (employee_code);
CREATE INDEX IF NOT EXISTS idx_emp_tenant ON employees (tenant_id);
CREATE INDEX IF NOT EXISTS idx_emp_cpf ON employees (cpf);

CREATE TABLE IF NOT EXISTS payrolls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    reference_month INTEGER NOT NULL,
    reference_year INTEGER NOT NULL,
    payroll_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_gross NUMERIC(19,2) DEFAULT 0,
    total_deductions NUMERIC(19,2) DEFAULT 0,
    total_net NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_payroll_ref ON payrolls (reference_month, reference_year);
CREATE INDEX IF NOT EXISTS idx_payroll_tenant ON payrolls (tenant_id);

CREATE TABLE IF NOT EXISTS payroll_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    payroll_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    code VARCHAR(50),
    CONSTRAINT fk_payroll_items_payroll FOREIGN KEY (payroll_id) REFERENCES payrolls (id),
    CONSTRAINT fk_payroll_items_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE IF NOT EXISTS time_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    record_date DATE NOT NULL,
    entry_time TIME,
    exit_time TIME,
    break_duration_minutes INTEGER,
    total_hours NUMERIC(19,2),
    type VARCHAR(20),
    notes VARCHAR(500),
    CONSTRAINT fk_time_records_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE INDEX IF NOT EXISTS idx_time_employee ON time_records (employee_id);
CREATE INDEX IF NOT EXISTS idx_time_date ON time_records (record_date);
CREATE INDEX IF NOT EXISTS idx_time_tenant ON time_records (tenant_id);

CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    user_name VARCHAR(255),
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(100),
    entity_id UUID,
    action_date TIMESTAMP NOT NULL,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    old_values TEXT,
    new_values TEXT,
    description VARCHAR(1000)
);

CREATE INDEX IF NOT EXISTS idx_audit_user ON audit_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_audit_date ON audit_logs (action_date);
CREATE INDEX IF NOT EXISTS idx_audit_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_tenant ON audit_logs (tenant_id);

CREATE TABLE IF NOT EXISTS accounting_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    document_number VARCHAR(50) NOT NULL UNIQUE,
    document_date DATE NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_debit NUMERIC(19,2) DEFAULT 0,
    total_credit NUMERIC(19,2) DEFAULT 0,
    posted_at TIMESTAMP,
    posted_by UUID
);

CREATE INDEX IF NOT EXISTS idx_doc_number ON accounting_documents (document_number);
CREATE INDEX IF NOT EXISTS idx_doc_tenant ON accounting_documents (tenant_id);
CREATE INDEX IF NOT EXISTS idx_doc_date ON accounting_documents (document_date);

CREATE TABLE IF NOT EXISTS accounting_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    tenant_id UUID NOT NULL,
    document_id UUID NOT NULL,
    entry_date DATE NOT NULL,
    description VARCHAR(500) NOT NULL,
    account_id UUID NOT NULL,
    cost_center_id UUID,
    entry_type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    reference VARCHAR(100),
    notes TEXT,
    CONSTRAINT fk_accounting_entries_document FOREIGN KEY (document_id) REFERENCES accounting_documents (id),
    CONSTRAINT fk_accounting_entries_account FOREIGN KEY (account_id) REFERENCES chart_of_accounts (id),
    CONSTRAINT fk_accounting_entries_cost_center FOREIGN KEY (cost_center_id) REFERENCES cost_centers (id)
);

CREATE INDEX IF NOT EXISTS idx_entry_date ON accounting_entries (entry_date);
CREATE INDEX IF NOT EXISTS idx_entry_tenant ON accounting_entries (tenant_id);
CREATE INDEX IF NOT EXISTS idx_entry_document ON accounting_entries (document_id);
