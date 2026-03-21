package br.com.sap.erp.modules.hcm.mapper;

import br.com.sap.erp.modules.hcm.domain.entity.Employee;
import br.com.sap.erp.modules.hcm.domain.entity.TimeRecord;
import br.com.sap.erp.modules.hcm.dto.EmployeeRequest;
import br.com.sap.erp.modules.hcm.dto.EmployeeResponse;
import br.com.sap.erp.modules.hcm.dto.TimeRecordRequest;
import br.com.sap.erp.modules.hcm.dto.TimeRecordResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface HumanResourcesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    Employee toEntity(EmployeeRequest request);

    EmployeeResponse toResponse(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "employee", expression = "java(toEmployeeReference(request.employeeId()))")
    TimeRecord toEntity(TimeRecordRequest request);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeCode", source = "employee.employeeCode")
    @Mapping(target = "employeeName", source = "employee.name")
    TimeRecordResponse toResponse(TimeRecord entity);

    default Employee toEmployeeReference(java.util.UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(employeeId);
        return employee;
    }
}
