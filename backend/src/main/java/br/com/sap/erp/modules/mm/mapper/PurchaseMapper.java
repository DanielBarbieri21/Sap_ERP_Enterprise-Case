package br.com.sap.erp.modules.mm.mapper;

import br.com.sap.erp.core.exception.ResourceNotFoundException;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrder;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseOrderItem;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisition;
import br.com.sap.erp.modules.mm.domain.entity.PurchaseRequisitionItem;
import br.com.sap.erp.modules.mm.dto.PurchaseItemRequest;
import br.com.sap.erp.modules.mm.dto.PurchaseItemResponse;
import br.com.sap.erp.modules.mm.dto.PurchaseOrderRequest;
import br.com.sap.erp.modules.mm.dto.PurchaseOrderResponse;
import br.com.sap.erp.modules.mm.dto.PurchaseRequisitionRequest;
import br.com.sap.erp.modules.mm.dto.PurchaseRequisitionResponse;
import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class PurchaseMapper {

    private final ProductRepository productRepository;

    public PurchaseMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PurchaseRequisition toEntity(PurchaseRequisitionRequest request) {
        PurchaseRequisition requisition = PurchaseRequisition.builder()
                .requisitionNumber(defaultValue(request.requisitionNumber(), "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()))
                .requisitionDate(request.requisitionDate() != null ? request.requisitionDate() : LocalDate.now())
                .description(request.description())
                .build();

        if (request.items() != null) {
            request.items().forEach(item -> requisition.addItem(toRequisitionItem(item)));
        }
        requisition.recalculateTotal();
        return requisition;
    }

    public PurchaseOrder toEntity(PurchaseOrderRequest request) {
        PurchaseOrder order = PurchaseOrder.builder()
                .orderNumber(defaultValue(request.orderNumber(), "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()))
                .orderDate(request.orderDate() != null ? request.orderDate() : LocalDate.now())
                .expectedDeliveryDate(request.expectedDeliveryDate())
                .supplierId(request.supplierId())
                .supplierName(defaultValue(request.supplierName(), "Fornecedor demo"))
                .discountAmount(defaultAmount(request.discountAmount()))
                .taxAmount(defaultAmount(request.taxAmount()))
                .build();

        if (request.items() != null) {
            request.items().forEach(item -> order.addItem(toOrderItem(item)));
        }
        order.recalculateTotals();
        return order;
    }

    public PurchaseRequisitionResponse toResponse(PurchaseRequisition entity) {
        List<PurchaseItemResponse> items = entity.getItems().stream()
                .map(this::toResponse)
                .toList();
        return new PurchaseRequisitionResponse(
                entity.getId(),
                entity.getRequisitionNumber(),
                entity.getRequisitionDate(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getTotalAmount(),
                items
        );
    }

    public PurchaseOrderResponse toResponse(PurchaseOrder entity) {
        List<PurchaseItemResponse> items = entity.getItems().stream()
                .map(this::toResponse)
                .toList();
        return new PurchaseOrderResponse(
                entity.getId(),
                entity.getOrderNumber(),
                entity.getOrderDate(),
                entity.getExpectedDeliveryDate(),
                entity.getSupplierId(),
                entity.getSupplierName(),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getDiscountAmount(),
                entity.getTaxAmount(),
                entity.getFinalAmount(),
                items
        );
    }

    private PurchaseRequisitionItem toRequisitionItem(PurchaseItemRequest request) {
        Product product = loadProduct(request.productId());
        BigDecimal unitPrice = defaultAmount(request.unitPrice());
        BigDecimal quantity = defaultAmount(request.quantity());
        return PurchaseRequisitionItem.builder()
                .productCode(product.getCode())
                .productDescription(product.getName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .unit(product.getUnit())
                .totalAmount(quantity.multiply(unitPrice))
                .build();
    }

    private PurchaseOrderItem toOrderItem(PurchaseItemRequest request) {
        Product product = loadProduct(request.productId());
        BigDecimal unitPrice = defaultAmount(request.unitPrice());
        BigDecimal quantity = defaultAmount(request.quantity());
        return PurchaseOrderItem.builder()
                .productCode(product.getCode())
                .productDescription(product.getName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .unit(product.getUnit())
                .totalAmount(quantity.multiply(unitPrice))
                .build();
    }

    private PurchaseItemResponse toResponse(PurchaseRequisitionItem item) {
        return new PurchaseItemResponse(
                item.getId(),
                item.getProductCode(),
                item.getProductDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalAmount(),
                item.getUnit()
        );
    }

    private PurchaseItemResponse toResponse(PurchaseOrderItem item) {
        return new PurchaseItemResponse(
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
