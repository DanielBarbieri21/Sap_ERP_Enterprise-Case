package br.com.sap.erp.modules.wm.mapper;

import br.com.sap.erp.modules.wm.domain.entity.Product;
import br.com.sap.erp.modules.wm.domain.entity.StockMovement;
import br.com.sap.erp.modules.wm.dto.ProductRequest;
import br.com.sap.erp.modules.wm.dto.ProductResponse;
import br.com.sap.erp.modules.wm.dto.StockMovementRequest;
import br.com.sap.erp.modules.wm.dto.StockMovementResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface WarehouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "product", expression = "java(toProductReference(request.productId()))")
    StockMovement toEntity(StockMovementRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    StockMovementResponse toResponse(StockMovement entity);

    default Product toProductReference(java.util.UUID id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
