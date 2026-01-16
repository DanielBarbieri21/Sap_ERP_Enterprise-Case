package br.com.sap.erp.modules.base.controller;

import br.com.sap.erp.core.domain.TenantContext;
import br.com.sap.erp.core.service.JwtService;
import br.com.sap.erp.modules.base.domain.entity.User;
import br.com.sap.erp.modules.base.dto.AuthRequest;
import br.com.sap.erp.modules.base.dto.AuthResponse;
import br.com.sap.erp.modules.base.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = (User) authentication.getPrincipal();
        UUID tenantId = user.getCompany().getId();
        
        TenantContext.setCurrentTenantId(tenantId);
        
        String token = jwtService.generateToken(user, tenantId);
        
        userService.updateLastLogin(user.getId());
        
        AuthResponse response = AuthResponse.builder()
            .token(token)
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .companyId(user.getCompany().getId())
            .companyName(user.getCompany().getName())
            .tenantId(tenantId)
            .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String email = jwtService.extractUsername(jwt);
            User user = (User) userService.loadUserByUsername(email);
            boolean isValid = jwtService.isTokenValid(jwt, user);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
