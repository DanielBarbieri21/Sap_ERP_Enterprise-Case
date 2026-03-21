package br.com.sap.erp.modules.sd.mapper;

import br.com.sap.erp.core.exception.ResourceNotFoundException;
import br.com.sap.erp.modules.sd.domain.entity.Invoice;
import br.com.sap.erp.modules.sd.domain.entity.InvoiceItem;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrder;
import br.com.sap.erp.modules.sd.domain.entity.SalesOrderItem;
import br.com.sap.erp.modules.sd.dto.InvoiceRequest;
import br.com.sap.erp.modules.sd.dto.InvoiceResponse;
import br.com.sap.erp.modules.sd.dto.SalesItemRequest;
import br.com.sap.erp.modules.sd.dto.SalesItemResponse;
import br.com.sap.erp.modules.sd.dto.SalesOrderRequest;
import br.com.sap.erp.modules.sd.dto.SalesOrderResponse;
import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class SalesMapper {

    private final ProductRepository productRepository;

    public SalesMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public SalesOrder toEntity(SalesOrderRequest request) {
        SalesOrder order = SalesOrder.builder()
                .orderNumber(defaultValue(request.orderNumber(), "SO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()))
                .orderDate(request.orderDate() != null ? request.orderDate() : LocalDate.now())
                .customerId(request.customerId())
                .customerName(defaultValue(request.customerName(), "Cliente demo"))
                .discountAmount(defaultAmount(request.discountAmount()))
                .taxAmount(defaultAmount(request.taxAmount()))
                .build();

        if (request.items() != null) {
            request.items().forEach(item -> order.addItem(toSalesOrderItem(item)));
        }
        order.recalculateTotals();
        return order;
    }

    public Invoice toEntity(InvoiceRequest request) {
        Invoice invoice = Invoice.builder()
                .invoiceNumber(defaultValue(request.invoiceNumber(), "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()))
                .invoiceDate(request.invoiceDate() != null ? request.invoiceDate() : LocalDate.now())
                .customerId(request.customerId())
                .customerName(defaultValue(request.customerName(), "Cliente demo"))
                .salesOrderId(request.salesOrderId())
                .taxAmount(defaultAmount(request.taxAmount()))
                .build();

        if (request.items() != null) {
            request.items().forEach(item -> invoice.addItem(toInvoiceItem(item)));
        }
        invoice.recalculateTotals();
        return invoice;
    }

    public SalesOrderResponse toResponse(SalesOrder entity) {
        List<SalesItemResponse> items = entity.getItems().stream()
                .map(this::toResponse)
                .toList();
        return new SalesOrderResponse(
                entity.getId(),
                entity.getOrderNumber(),
                entity.getOrderDate(),
                entity.getCustomerId(),
                entity.getCustomerName(),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getDiscountAmount(),
                entity.getTaxAmount(),
                entity.getFinalAmount(),
                items
        );
    }

    public InvoiceResponse toResponse(Invoice entity) {
        List<SalesItemResponse> items = entity.getItems().stream()
                .map(this::toResponse)
                .toList();
        return new InvoiceResponse(
                entity.getId(),
                entity.getInvoiceNumber(),
                entity.getInvoiceDate(),
                entity.getCustomerId(),
                entity.getCustomerName(),
                entity.getSalesOrderId(),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getTaxAmount(),
                entity.getFinalAmount(),
                items
        );
    }

    private SalesOrderItem toSalesOrderItem(SalesItemRequest request) {
        Product product = loadProduct(request.productId());
        BigDecimal quantity = defaultAmount(request.quantity());
        BigDecimal unitPrice = defaultAmount(request.unitPrice());
        return SalesOrderItem.builder()
                .productCode(product.getCode())
                .productDescription(product.getName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalAmount(quantity.multiply(unitPrice))
                .unit(product.getUnit())
                .build();
    }

    private InvoiceItem toInvoiceItem(SalesItemRequest request) {
        Product product = loadProduct(request.productId());
        BigDecimal quantity = defaultAmount(request.quantity());
        BigDecimal unitPrice = defaultAmount(request.unitPrice());
        return InvoiceItem.builder()
                .productCode(product.getCode())
                .productDescription(product.getName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalAmount(quantity.multiply(unitPrice))
                .unit(product.getUnit())
                .build();
    }

    private SalesItemResponse toResponse(SalesOrderItem item) {
        return new SalesItemResponse(
                item.getId(),
                item.getProductCode(),
                item.getProductDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalAmount(),
                item.getUnit()
        );
    }

    private SalesItemResponse toResponse(InvoiceItem item) {
        return new SalesItemResponse(
                item.getId(),
                item.getProductCode(),
                item.getProductDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalAmount(),
                item.getUnit()
        );
    }

    private Product loadProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado"));
    }

    private String defaultValue(String value, String fallback) {
        return value != null && !value.isBlank() ? value : fallback;
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
