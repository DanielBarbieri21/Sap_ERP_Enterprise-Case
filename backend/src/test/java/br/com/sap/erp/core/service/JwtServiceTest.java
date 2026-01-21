package br.com.sap.erp.core.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService createService(String secret, long expiration) throws Exception {
        JwtService svc = new JwtService();
        Field f1 = JwtService.class.getDeclaredField("secretKey");
        f1.setAccessible(true);
        f1.set(svc, secret);
        Field f2 = JwtService.class.getDeclaredField("jwtExpiration");
        f2.setAccessible(true);
        f2.setLong(svc, expiration);
        return svc;
    }

    @Test
    void generateAndValidateToken() throws Exception {
        String secret = "dGhpc2lzYXNlY3VyZWp3dHNlY3JldGtleWZvcnNhcGVycHN5c3RlbWRvbm90dXNlaW5wcm9kdWN0aW9u";
        long expiration = 3_600_000L; // 1h
        JwtService jwtService = createService(secret, expiration);

        UserDetails userDetails = new User("admin@demo.com", "password", Collections.emptyList());
        UUID tenantId = UUID.fromString("a5a41c52-91d1-4164-9dc7-9d5fbe4dac06");

        String token = jwtService.generateToken(userDetails, tenantId);
        assertNotNull(token);

        assertEquals("admin@demo.com", jwtService.extractUsername(token));
        assertEquals(tenantId, jwtService.extractTenantId(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
