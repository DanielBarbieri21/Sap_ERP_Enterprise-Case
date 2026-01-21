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

        // Valores antes (opcional simples)
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
