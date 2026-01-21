package br.com.sap.erp.core.audit;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.modules.base.domain.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final br.com.sap.erp.core.messaging.AuditEventPublisher auditEventPublisher;

    @Transactional
    public void logAction(String action, String entityType, UUID entityId, 
                         Object oldValue, Object newValue, HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UUID userId = null;
            String userName = null;
            
            if (auth != null && auth.getPrincipal() instanceof User) {
                User user = (User) auth.getPrincipal();
                userId = user.getId();
                userName = user.getName();
            }
            
            AuditLog log = AuditLog.builder()
                    .userId(userId)
                    .userName(userName)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .actionDate(LocalDateTime.now())
                    .ipAddress(request != null ? getClientIp(request) : null)
                    .userAgent(request != null ? request.getHeader("User-Agent") : null)
                    .oldValues(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null)
                    .newValues(newValue != null ? objectMapper.writeValueAsString(newValue) : null)
                    .build();
            
            UUID tenantId = TenantContext.getCurrentTenantId();
            if (tenantId != null) {
                log.setTenantId(tenantId);
            }
            
            auditLogRepository.save(log);
            // Publica evento de auditoria (assíncrono)
            auditEventPublisher.publish(log);
        } catch (Exception e) {
            // Log error but don't break the main flow
            System.err.println("Erro ao registrar auditoria: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        UUID tenantId = TenantContext.getCurrentTenantId();
        if (tenantId == null) {
            return List.of();
        }
        return auditLogRepository.findByDateRange(tenantId, startDate, endDate);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
