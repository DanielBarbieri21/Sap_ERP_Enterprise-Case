package br.com.sap.erp.core.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final ThreadLocal<Map<String, Object>> beforeState = new ThreadLocal<>();

    @Before("@annotation(br.com.sap.erp.core.audit.Auditable)")
    public void beforeMethod(JoinPoint joinPoint) {
        Map<String, Object> state = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            state.put("args", args[0]);
        }
        beforeState.set(state);
    }

    @AfterReturning(pointcut = "@annotation(br.com.sap.erp.core.audit.Auditable)", returning = "result")
    public void afterMethod(JoinPoint joinPoint, Object result) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes()).getRequest();
            
            String action = joinPoint.getSignature().getName().toUpperCase();
            String entityType = result != null ? result.getClass().getSimpleName() : "Unknown";
            UUID entityId = extractEntityId(result);
            
            Object oldValue = beforeState.get() != null ? beforeState.get().get("args") : null;
            
            auditService.logAction(action, entityType, entityId, oldValue, result, request);
        } catch (Exception e) {
            // Ignore audit errors
        } finally {
            beforeState.remove();
        }
    }

    private UUID extractEntityId(Object entity) {
        if (entity == null) return null;
        try {
            if (entity instanceof br.com.sap.erp.core.domain.BaseEntity) {
                return ((br.com.sap.erp.core.domain.BaseEntity) entity).getId();
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
