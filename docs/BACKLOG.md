# Backlog Módulos e Integrações

## MM (Compras)
- Entidades: `Supplier`, `PurchaseRequisition`, `PurchaseOrder`, `GoodsReceipt`
- Endpoints: criar/listar/autorizar pedidos; registrar recebimento
- Regras: validação fornecedor, status workflow (draft/approved/received)

## SD (Vendas)
- Entidades: `Customer`, `SalesOrder`, `Invoice`
- Endpoints: criar/listar pedidos; faturar; emitir nota
- Regras: estoque disponível, cálculo de impostos

## WM (Estoque)
- Entidades: `Product`, `StockMovement`, `Inventory`, `Location`
- Endpoints: movimentar; inventariar; listar baixo estoque
- Regras: endereçamento, lotes/validade, bloqueio por inventário

## CO (Contábil)
- Entidades: `AccountingDocument`, `AccountingEntry`, `CostCenter`
- Endpoints: criar documento; lançar; relatórios (balancete, DRE, BP)
- Regras: partidas dobradas, validação contas (FI)

## HCM (RH)
- Entidades: `Employee`, `Payroll`, `TimeRecord`
- Endpoints: cadastro; folha; ponto; histórico
- Regras: jornadas, cálculos básicos folha

## Integrações
- PIX: `PixClient` com providers; webhooks
- Boletos: emissão e consulta; remessa/retorno (CNAB)
- NFe: emissão, status, XML/assinatura; SEFAZ

## Dashboard
- KPIs: receitas/despesas no mês, pedidos (SD/MM), estoque baixo
- Filtros por período/empresa (tenant)

## Relatórios (Jasper)
- Templates: extratos financeiros, pedidos, estoque
- Serviço: compilar/preencher, exportar PDF/Excel
