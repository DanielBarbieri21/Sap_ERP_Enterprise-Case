package br.com.sap.erp.core.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(br.com.sap.erp.core.audit.Auditable)")
    public Object aroundAuditable(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Auditable auditable = signature.getMethod().getAnnotation(Auditable.class);
        String action = auditable != null ? auditable.value() : "ACTION";
        String entityType = signature.getDeclaringType().getSimpleName();

        // Obtém request atual (se existir)
        HttpServletRequest request = null;
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            request = servletAttrs.getRequest();
        }

        // Valores antes (opcional  simples)
        Object oldValue = null;

        try {
            result = pjp.proceed();
        } catch (Throwable ex) {
            // Em caso de erro também registramos
            auditService.logAction(action, entityType, (UUID) null, oldValue, null, request);
            throw ex;
        }

        // Após execução, registra com resultado como newValue
        auditService.logAction(action, entityType, (UUID) null, oldValue, result, request);
        return result;
    }
}
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
